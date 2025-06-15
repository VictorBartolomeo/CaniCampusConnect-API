package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RestController
@IsClubOwner
public class RegistrationController {

    // Groupe de validation pour mise à jour du statut uniquement
    public interface StatusUpdateValidation extends Default {}

    protected RegistrationService registrationService;

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
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        boolean deleted = registrationService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update only the status of a registration
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