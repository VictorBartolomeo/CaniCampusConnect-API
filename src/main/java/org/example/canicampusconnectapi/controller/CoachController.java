package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Positive;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.service.registration.RegistrationService;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class CoachController {

    private final RegistrationService registrationService;
    private final CoachDao coachDao;

    public CoachController(RegistrationService registrationService, CoachDao coachDao) {
        this.registrationService = registrationService;
        this.coachDao = coachDao;
    }



    @GetMapping("/coach/{id}")

    public ResponseEntity<Coach> getCoach(@PathVariable Long id) {

        Optional<Coach> optionalCoach = coachDao.findById(id);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalCoach.get(), HttpStatus.OK);

    }


    @GetMapping("/coachs")
    public List<Coach> getAll() {
        return coachDao.findAll();
    }

    @PostMapping("/coach")
    public ResponseEntity<Coach> createCoach(@RequestBody Coach coach) {
        coachDao.save(coach);
        return new ResponseEntity<>(coach, HttpStatus.CREATED);
    }

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
    @PutMapping("/coach/{id}")
    // Patch change une partie de l'objet
//    @PatchMapping("/coach/{id}")
    public ResponseEntity<Coach> updateCoach(@PathVariable Long id, @RequestBody Coach coach) {
        Optional<Coach> optionalCoach = coachDao.findById(id);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        coach.setId(id);
        coachDao.save(coach);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
