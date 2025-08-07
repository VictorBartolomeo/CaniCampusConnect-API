package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.example.canicampusconnectapi.model.courseRelated.AgeRange;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.dog.DogService;
import org.example.canicampusconnectapi.service.registration.RegistrationService;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.example.canicampusconnectapi.view.registration.AdminViewRegistration;
import org.example.canicampusconnectapi.view.registration.CoachViewRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@IsClubOwner
@RequiredArgsConstructor
public class RegistrationController {

    // Groupe de validation pour mise à jour du statut uniquement
    public interface StatusUpdateValidation extends Default {}

    protected RegistrationService registrationService;
    protected DogService dogService;

    @Autowired
    public RegistrationController(org.example.canicampusconnectapi.service.registration.RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // Get a registration by ID
    @IsOwner
    @GetMapping("/registration/{id}")
    @JsonView(OwnerView.class)
    public ResponseEntity<Registration> getRegistration(@PathVariable Long id) {
        return registrationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all registrations
    @GetMapping("/registrations")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getAllRegistrations() {
        return registrationService.findAll();
    }

    // Get registrations by dog
    @IsOwner
    @GetMapping("/dog/{dogId}/registrations")
    @JsonView(OwnerView.class)
    public ResponseEntity<List<Registration>> getRegistrationsByDog(@PathVariable Long dogId) {
        List<Registration> registrations = registrationService.findByDogId(dogId);
        return ResponseEntity.ok(registrations);
    }

    // Get registrations by course
    @IsOwner
    @JsonView(OwnerViewDog.class)
    @GetMapping("/course/{courseId}/registrations")
    public ResponseEntity<List<Registration>> getRegistrationsByCourse(@PathVariable Long courseId) {
        List<Registration> registrations = registrationService.findByCourseId(courseId);
        return ResponseEntity.ok(registrations);
    }

    // Get registrations by status
    @GetMapping("/registrations/status/{status}")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getRegistrationsByStatus(@PathVariable RegistrationStatus status) {
        return registrationService.findByStatus(status);
    }

    // Get registrations by date
    @GetMapping("/registrations/date")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getRegistrationsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant date) {
        return registrationService.findByRegistrationDate(date);
    }

    // Get registrations by date range
    @GetMapping("/registrations/between")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getRegistrationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        return registrationService.findByRegistrationDateBetween(start, end);
    }

    // Get upcoming registrations
    @IsCoach
    @GetMapping("/registrations/upcoming")
    @JsonView(CoachViewRegistration.class)
    public List<Registration> getUpcomingRegistrations() {
        return registrationService.findUpcoming();
    }

    // Get upcoming registrations for a specific dog
    @IsOwner
    @GetMapping("/dog/{dogId}/registrations/upcoming")
    @JsonView(OwnerView.class)
    public ResponseEntity<List<Registration>> getUpcomingRegistrationsByDog(@PathVariable Long dogId) {
        List<Registration> registrations = registrationService.findUpcomingByDogId(dogId);
        return ResponseEntity.ok(registrations);
    }

    // Get past registrations
    @IsCoach
    @GetMapping("/registrations/past")
    @JsonView(CoachViewRegistration.class)
    public List<Registration> getPastRegistrations() {
        return registrationService.findPast();
    }

    // Get past registrations for a specific dog
    @IsOwner
    @GetMapping("/dog/{dogId}/registrations/past")
    @JsonView(OwnerView.class)
    public ResponseEntity<List<Registration>> getPastRegistrationsByDog(@PathVariable Long dogId) {
        List<Registration> registrations = registrationService.findPastByDogId(dogId);
        return ResponseEntity.ok(registrations);
    }

    // Get current registrations
    @IsCoach
    @GetMapping("/registrations/current")
    @JsonView(CoachViewRegistration.class)
    public List<Registration> getCurrentRegistrations() {
        return registrationService.findCurrent();
    }

    // Get current registrations for a specific dog
    @IsOwner
    @GetMapping("/dog/{dogId}/registrations/current")
    @JsonView(OwnerView.class)
    public ResponseEntity<List<Registration>> getCurrentRegistrationsByDog(@PathVariable Long dogId) {
        List<Registration> registrations = registrationService.findCurrentByDogId(dogId);
        return ResponseEntity.ok(registrations);
    }

    // Count registrations for a course
    @IsOwner
    @JsonView(OwnerViewDog.class)
    @GetMapping("/course/{courseId}/registrations/count")
    public ResponseEntity<Long> countRegistrationsByCourse(@PathVariable Long courseId) {
        long count = registrationService.countByCourseId(courseId);
        return ResponseEntity.ok(count);
    }

    // Create a new registration
    @IsOwner
    @PostMapping("/registration")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Registration> createRegistration(@RequestBody @Valid Registration registration) {
        try {
            Registration createdRegistration = registrationService.create(registration);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRegistration);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }


    // Update a registration
    @IsCoach
    @PutMapping("/registration/{id}")
    @JsonView(CoachView.class)
    public ResponseEntity<Registration> updateRegistration(
            @PathVariable Long id,
            @RequestBody @Valid Registration registrationDetails) {
        try {
            return registrationService.update(id, registrationDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    // Delete a registration
    @DeleteMapping("/registration/{id}")
    public ResponseEntity<Void> deleteRegistration(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        try {
            Optional<Registration> registrationOpt = registrationService.findById(id);
            if (registrationOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Registration registration = registrationOpt.get();

            if (userDetails == null || userDetails.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            boolean isClubOwner = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLUB_OWNER"));

            if (isClubOwner) {
                boolean deleted = registrationService.deleteById(id);
                return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
            } else {
                if (!registration.getDog().getOwner().getId().equals(userDetails.getUserId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                if (registration.getCourse().getStartDatetime().isBefore(Instant.now())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Cours déjà commencé
                }

                boolean deleted = registrationService.deleteById(id);
                return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @IsCoach
    @PatchMapping("/registration/{id}/status")
    @JsonView(CoachView.class)
    public ResponseEntity<Registration> updateRegistrationStatus(
            @PathVariable Long id,
            @RequestBody @Validated({Registration.RegistrationStatusValidation.class}) Registration statusUpdate) {
        try {
            if (statusUpdate.getStatus() == null) {
                return ResponseEntity.badRequest().build();
            }

            return registrationService.updateStatus(id, statusUpdate.getStatus())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }
}