package org.example.canicampusconnectapi.controller;

// Imports inchangés sauf pour les DAOs qui ne sont plus directement utilisés ici
import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.registration.RegistrationService; // Importer le service
import org.example.canicampusconnectapi.view.registration.AdminViewRegistration;
import org.example.canicampusconnectapi.view.registration.CoachViewRegistration;
import org.example.canicampusconnectapi.view.registration.OwnerViewRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Pour gérer les Optional vides

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
public class RegistrationController {

    // Injecter le service au lieu des DAOs
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
        // Les injections des DAOs sont supprimées
    }

    // Get a registration by ID
    @IsOwner
    @GetMapping("/registration/{id}")
    @JsonView(OwnerViewRegistration.class)
    public ResponseEntity<Registration> getRegistration(@PathVariable Long id) {
        return registrationService.findById(id)
                .map(ResponseEntity::ok) // Si trouvé, retourne 200 OK avec l'objet
                .orElse(ResponseEntity.notFound().build()); // Sinon, retourne 404 Not Found
    }

    // Get all registrations
    @IsClubOwner
    @GetMapping("/registrations")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getAllRegistrations() {
        // Pas besoin de ResponseEntity ici si retourner une liste (vide ou non) est acceptable
        return registrationService.findAll();
    }

    // Get registrations by dog
    @IsOwner
    @GetMapping("/dog/{dogId}/registrations")
    @JsonView(OwnerViewRegistration.class)
    public ResponseEntity<List<Registration>> getRegistrationsByDog(@PathVariable Long dogId) {
        List<Registration> registrations = registrationService.findByDogId(dogId);
        // On pourrait vérifier si le chien existe avant, mais le service peut aussi le faire
        // Retourne OK même si la liste est vide, c'est le comportement standard
        return ResponseEntity.ok(registrations);
    }

    // Get registrations by course
    @IsCoach
    @GetMapping("/course/{courseId}/registrations")
    @JsonView(CoachViewRegistration.class)
    public ResponseEntity<List<Registration>> getRegistrationsByCourse(@PathVariable Long courseId) {
        List<Registration> registrations = registrationService.findByCourseId(courseId);
        return ResponseEntity.ok(registrations);
    }

    // Get registrations by course type
    @IsCoach
    @GetMapping("/coursetype/{courseTypeId}/registrations")
    @JsonView(CoachViewRegistration.class)
    public ResponseEntity<List<Registration>> getRegistrationsByCourseType(@PathVariable Long courseTypeId) {
        List<Registration> registrations = registrationService.findByCourseTypeId(courseTypeId);
        return ResponseEntity.ok(registrations);
    }

    // Get registrations by status
    @IsClubOwner
    @GetMapping("/registrations/status/{status}")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getRegistrationsByStatus(@PathVariable RegistrationStatus status) {
        return registrationService.findByStatus(status);
    }

    // Get registrations by date
    @IsClubOwner
    @GetMapping("/registrations/date")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getRegistrationsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return registrationService.findByRegistrationDate(date);
    }

    // Get registrations by date range
    @IsClubOwner
    @GetMapping("/registrations/between")
    @JsonView(AdminViewRegistration.class)
    public List<Registration> getRegistrationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
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
    @JsonView(OwnerViewRegistration.class)
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
    @JsonView(OwnerViewRegistration.class)
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
    @JsonView(OwnerViewRegistration.class)
    public ResponseEntity<List<Registration>> getCurrentRegistrationsByDog(@PathVariable Long dogId) {
        List<Registration> registrations = registrationService.findCurrentByDogId(dogId);
        return ResponseEntity.ok(registrations);
    }

    // Count registrations for a course
    @IsCoach
    @GetMapping("/course/{courseId}/registrations/count")
    public ResponseEntity<Long> countRegistrationsByCourse(@PathVariable Long courseId) {
        // On pourrait vérifier si le cours existe ici ou dans le service
        long count = registrationService.countByCourseId(courseId);
        return ResponseEntity.ok(count);
    }

    // Create a new registration
    @IsOwner
    @PostMapping("/registration")
    @JsonView(OwnerViewRegistration.class)
    public ResponseEntity<Registration> createRegistration(@RequestBody Registration registration) {
        try {
            Registration createdRegistration = registrationService.create(registration);
            // Retourne 201 Created avec l'objet créé
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRegistration);
        } catch (IllegalArgumentException e) {
            // Erreur de validation simple (ex: ID manquant)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) {
            // Autre erreur métier (ex: cours plein, déjà inscrit)
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
            // Ou BAD_REQUEST selon la nature de l'erreur
        }
    }

    // Update an existing registration (exemple: mise à jour du statut)
    @IsCoach
    @PutMapping("/registration/{id}")
    @JsonView(CoachViewRegistration.class)
    public ResponseEntity<Registration> updateRegistration(@PathVariable Long id, @RequestBody Registration registrationDetails) {
        // Le corps de la requête contient les détails à mettre à jour (ex: juste le statut)
        return registrationService.update(id, registrationDetails)
                .map(ResponseEntity::ok) // Retourne 200 OK avec l'objet mis à jour
                .orElse(ResponseEntity.notFound().build()); // Retourne 404 si non trouvé
    }

    // Delete a registration
    @IsClubOwner
    @DeleteMapping("/registration/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        boolean deleted = registrationService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // Retourne 204 No Content si supprimé
        } else {
            return ResponseEntity.notFound().build(); // Retourne 404 si non trouvé
        }
    }
}
