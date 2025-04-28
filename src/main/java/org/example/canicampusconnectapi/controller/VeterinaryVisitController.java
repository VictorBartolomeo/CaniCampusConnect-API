package org.example.canicampusconnectapi.controller;

import jakarta.persistence.EntityNotFoundException;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.example.canicampusconnectapi.service.VeterinaryVisitService; // Importer le service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
public class VeterinaryVisitController {

    private final VeterinaryVisitService veterinaryVisitService; // Injecter le service

    @Autowired
    public VeterinaryVisitController(VeterinaryVisitService veterinaryVisitService) {
        this.veterinaryVisitService = veterinaryVisitService;
        // Les injections de DAO sont supprimées
    }

    @GetMapping("/visit/{id}")
    public ResponseEntity<VeterinaryVisit> getVisit(@PathVariable Long id) {
        return veterinaryVisitService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/visits")
    public List<VeterinaryVisit> getAllVisits() {
        return veterinaryVisitService.findAll();
    }

    @GetMapping("/dog/{dogId}/visits")
    public ResponseEntity<List<VeterinaryVisit>> getVisitsByDog(@PathVariable Long dogId) {
        try {
            List<VeterinaryVisit> visits = veterinaryVisitService.findByDogIdOrderedByDateDesc(dogId);
            return ResponseEntity.ok(visits);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/visits/between")
    public List<VeterinaryVisit> getVisitsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return veterinaryVisitService.findByVisitDateBetween(startDate, endDate);
    }

    @GetMapping("/dog/{dogId}/visits/between")
    public ResponseEntity<List<VeterinaryVisit>> getVisitsByDogBetweenDates(
            @PathVariable Long dogId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            List<VeterinaryVisit> visits = veterinaryVisitService.findByDogIdAndVisitDateBetween(dogId, startDate, endDate);
            return ResponseEntity.ok(visits);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/visits/reason/{reason}")
    public List<VeterinaryVisit> getVisitsByReason(@PathVariable String reason) {
        return veterinaryVisitService.findByReasonContaining(reason);
    }

    @GetMapping("/visits/veterinarian/{veterinarian}")
    public List<VeterinaryVisit> getVisitsByVeterinarian(@PathVariable String veterinarian) {
        return veterinaryVisitService.findByVeterinarianContaining(veterinarian);
    }

    @GetMapping("/visits/diagnosis/{diagnosis}")
    public List<VeterinaryVisit> getVisitsByDiagnosis(@PathVariable String diagnosis) {
        return veterinaryVisitService.findByDiagnosisContaining(diagnosis);
    }

    @GetMapping("/visits/treatment/{treatment}")
    public List<VeterinaryVisit> getVisitsByTreatment(@PathVariable String treatment) {
        return veterinaryVisitService.findByTreatmentContaining(treatment);
    }

    @PostMapping("/visit")
    public ResponseEntity<VeterinaryVisit> createVisit(@RequestBody VeterinaryVisit visit) {
        try {
            VeterinaryVisit createdVisit = veterinaryVisitService.create(visit);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVisit);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e); // Ou BAD_REQUEST si l'ID chien fourni n'est pas trouvé
        }
    }

    @DeleteMapping("/visit/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        boolean deleted = veterinaryVisitService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PutMapping("/visit/{id}")
    public ResponseEntity<VeterinaryVisit> updateVisit(@PathVariable Long id, @RequestBody VeterinaryVisit visitDetails) {
        try {
            return veterinaryVisitService.update(id, visitDetails)
                    .map(ResponseEntity::ok) // 200 OK avec la visite mise à jour
                    .orElse(ResponseEntity.notFound().build()); // 404 si la visite avec cet ID n'existe pas
        } catch (EntityNotFoundException e) {
            // Si on essaie de mettre à jour avec un ID de chien qui n'existe pas
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            // Autres erreurs de validation potentielles lors de la mise à jour
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}