package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.MedicationTreatmentDao;
import org.example.canicampusconnectapi.model.Dog;
import org.example.canicampusconnectapi.model.healthRecord.MedicationTreatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class MedicationTreatmentController {

    protected MedicationTreatmentDao medicationTreatmentDao;
    protected DogDao dogDao;

    @Autowired
    public MedicationTreatmentController(MedicationTreatmentDao medicationTreatmentDao, DogDao dogDao) {
        this.medicationTreatmentDao = medicationTreatmentDao;
        this.dogDao = dogDao;
    }

    @GetMapping("/treatment/{id}")
    public ResponseEntity<MedicationTreatment> getTreatment(@PathVariable Long id) {
        Optional<MedicationTreatment> optionalTreatment = medicationTreatmentDao.findById(id);
        if (optionalTreatment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalTreatment.get(), HttpStatus.OK);
    }

    @GetMapping("/treatments")
    public List<MedicationTreatment> getAllTreatments() {
        return medicationTreatmentDao.findAll();
    }

    @GetMapping("/dog/{dogId}/treatments")
    public ResponseEntity<List<MedicationTreatment>> getTreatmentsByDog(@PathVariable Long dogId) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<MedicationTreatment> treatments = medicationTreatmentDao.findByDogOrderByStartDateDesc(optionalDog.get());
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }

    @GetMapping("/treatments/between")
    public List<MedicationTreatment> getTreatmentsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return medicationTreatmentDao.findByStartDateBetween(startDate, endDate);
    }

    @GetMapping("/dog/{dogId}/treatments/between")
    public ResponseEntity<List<MedicationTreatment>> getTreatmentsByDogBetweenDates(
            @PathVariable Long dogId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<MedicationTreatment> treatments = medicationTreatmentDao.findByDogAndStartDateBetween(optionalDog.get(), startDate, endDate);
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }

    @GetMapping("/treatments/active")
    public List<MedicationTreatment> getActiveTreatments() {
        return medicationTreatmentDao.findActiveTreatments();
    }

    @GetMapping("/dog/{dogId}/treatments/active")
    public ResponseEntity<List<MedicationTreatment>> getActiveTreatmentsByDog(@PathVariable Long dogId) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<MedicationTreatment> treatments = medicationTreatmentDao.findActiveTreatmentsByDog(optionalDog.get());
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }

    @GetMapping("/treatments/name/{name}")
    public List<MedicationTreatment> getTreatmentsByName(@PathVariable String name) {
        return medicationTreatmentDao.findByNameContaining(name);
    }

    @PostMapping("/treatment")
    public ResponseEntity<MedicationTreatment> createTreatment(@RequestBody MedicationTreatment treatment) {
        // Verify that the dog exists
        if (treatment.getDog() == null || treatment.getDog().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<Dog> optionalDog = dogDao.findById(treatment.getDog().getId());
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Set the dog to ensure the relationship is properly established
        treatment.setDog(optionalDog.get());
        
        medicationTreatmentDao.save(treatment);
        return new ResponseEntity<>(treatment, HttpStatus.CREATED);
    }

    @DeleteMapping("/treatment/{id}")
    public ResponseEntity<MedicationTreatment> deleteTreatment(@PathVariable Long id) {
        Optional<MedicationTreatment> optionalTreatment = medicationTreatmentDao.findById(id);
        if (optionalTreatment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicationTreatmentDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/treatment/{id}")
    public ResponseEntity<MedicationTreatment> updateTreatment(@PathVariable Long id, @RequestBody MedicationTreatment treatment) {
        Optional<MedicationTreatment> optionalTreatment = medicationTreatmentDao.findById(id);
        if (optionalTreatment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the dog exists if it's being updated
        if (treatment.getDog() != null && treatment.getDog().getId() != null) {
            Optional<Dog> optionalDog = dogDao.findById(treatment.getDog().getId());
            if (optionalDog.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            treatment.setDog(optionalDog.get());
        } else {
            // Keep the existing dog if not provided in the update
            treatment.setDog(optionalTreatment.get().getDog());
        }
        
        treatment.setId(id);
        medicationTreatmentDao.save(treatment);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}