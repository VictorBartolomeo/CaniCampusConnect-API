package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Positive;
import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.service.registration.RegistrationService;
import org.example.canicampusconnectapi.view.admin.AdminViewCoach;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@IsClubOwner
public class CoachController {

    protected RegistrationService registrationService;
    protected CoachDao coachDao;

    public CoachController(RegistrationService registrationService, CoachDao coachDao) {
        this.registrationService = registrationService;
        this.coachDao = coachDao;
    }



    @IsCoach
    @GetMapping("/coach/{id}")

    public ResponseEntity<Coach> getCoach(@PathVariable Long id) {

        Optional<Coach> optionalCoach = coachDao.findById(id);
        return optionalCoach
                .map(coach -> new ResponseEntity<>(coach, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/coaches")
    @JsonView(AdminViewCoach.class)
    public List<Coach> getAll() {
        return coachDao.findAll();
    }

    //J'ai mis le POST dans le AuthController avec les register Owner ca me parait plus pertinent

    @DeleteMapping("coach/{id}")
    public ResponseEntity<Coach> deleteCoach(@PathVariable Long id) {

        Optional<Coach> optionalCoach = coachDao.findById(id);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        coachDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Put change tout l'objet
    @IsCoach
    @PutMapping("/coach/{id}")
    @JsonView(AdminViewCoach.class)
    public ResponseEntity<Coach> updateCoach(@PathVariable Long id, @RequestBody @Validated(Coach.onUpdateCoach.class) Coach coach) {
        Optional<Coach> optionalCoach = coachDao.findById(id);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Coach existingCoach = optionalCoach.get();

        coach.setId(id);
        coach.setPassword(existingCoach.getPassword());
        coach.setEmailValidated(existingCoach.isEmailValidated());
        coach.setEmailValidatedAt(existingCoach.getEmailValidatedAt());

        Coach updatedCoach = coachDao.save(coach);
        updatedCoach.setPassword(null);

        return new ResponseEntity<>(updatedCoach, HttpStatus.OK);
    }


    /**
     * Récupère toutes les registrations en attente pour les cours FUTURS d'un coach.
     * Expire automatiquement les registrations des cours passés.
     */
    @IsCoach
    @GetMapping("/coach/{coachId}/pending-registrations")
    @JsonView(CoachViewRegistrations.class)
    public ResponseEntity<List<Registration>> getPendingRegistrations(
            @PathVariable @Positive(message = "L'ID du coach doit être positif") Long coachId) {

        // Vérifier que le coach existe
        if (!coachDao.existsById(coachId)) {
            return ResponseEntity.notFound().build();
        }

        // Récupérer les registrations en attente POUR DES COURS FUTURS uniquement
        // (expire automatiquement celles des cours passés)
        List<Registration> activePendingRegistrations =
                registrationService.findActivePendingRegistrationsByCoachId(coachId);

        return ResponseEntity.ok(activePendingRegistrations);
    }

    /**
     * Met à jour le statut d'une registration (pour valider ou refuser).
     */
    @IsCoach
    @PatchMapping("/registrations/{registrationId}/status")
    public ResponseEntity<Registration> updateRegistrationStatus(
            @PathVariable @Positive(message = "L'ID de la registration doit être positif") Long registrationId,
            @RequestBody @Validated(Registration.RegistrationStatusValidation.class) Registration statusUpdate) {

        Optional<Registration> updatedRegistration = registrationService.updateStatus(
                registrationId, statusUpdate.getStatus());

        return updatedRegistration
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
