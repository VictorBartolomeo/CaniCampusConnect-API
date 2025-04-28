package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.VeterinaryVisitDao;
import org.example.canicampusconnectapi.model.Dog;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
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
public class VeterinaryVisitController {

    protected VeterinaryVisitDao veterinaryVisitDao;
    protected DogDao dogDao;

    @Autowired
    public VeterinaryVisitController(VeterinaryVisitDao veterinaryVisitDao, DogDao dogDao) {
        this.veterinaryVisitDao = veterinaryVisitDao;
        this.dogDao = dogDao;
    }

    @GetMapping("/visit/{id}")
    public ResponseEntity<VeterinaryVisit> getVisit(@PathVariable Long id) {
        Optional<VeterinaryVisit> optionalVisit = veterinaryVisitDao.findById(id);
        if (optionalVisit.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalVisit.get(), HttpStatus.OK);
    }

    @GetMapping("/visits")
    public List<VeterinaryVisit> getAllVisits() {
        return veterinaryVisitDao.findAll();
    }

    @GetMapping("/dog/{dogId}/visits")
    public ResponseEntity<List<VeterinaryVisit>> getVisitsByDog(@PathVariable Long dogId) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<VeterinaryVisit> visits = veterinaryVisitDao.findByDogOrderByVisitDateDesc(optionalDog.get());
        return new ResponseEntity<>(visits, HttpStatus.OK);
    }

    @GetMapping("/visits/between")
    public List<VeterinaryVisit> getVisitsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return veterinaryVisitDao.findByVisitDateBetween(startDate, endDate);
    }

    @GetMapping("/dog/{dogId}/visits/between")
    public ResponseEntity<List<VeterinaryVisit>> getVisitsByDogBetweenDates(
            @PathVariable Long dogId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<VeterinaryVisit> visits = veterinaryVisitDao.findByDogAndVisitDateBetween(optionalDog.get(), startDate, endDate);
        return new ResponseEntity<>(visits, HttpStatus.OK);
    }

    @GetMapping("/visits/reason/{reason}")
    public List<VeterinaryVisit> getVisitsByReason(@PathVariable String reason) {
        return veterinaryVisitDao.findByReasonForVisitContaining(reason);
    }

    @GetMapping("/visits/veterinarian/{veterinarian}")
    public List<VeterinaryVisit> getVisitsByVeterinarian(@PathVariable String veterinarian) {
        return veterinaryVisitDao.findByVeterinarianContaining(veterinarian);
    }

    @GetMapping("/visits/diagnosis/{diagnosis}")
    public List<VeterinaryVisit> getVisitsByDiagnosis(@PathVariable String diagnosis) {
        return veterinaryVisitDao.findByDiagnosisContaining(diagnosis);
    }

    @GetMapping("/visits/treatment/{treatment}")
    public List<VeterinaryVisit> getVisitsByTreatment(@PathVariable String treatment) {
        return veterinaryVisitDao.findByTreatmentContaining(treatment);
    }

    @PostMapping("/visit")
    public ResponseEntity<VeterinaryVisit> createVisit(@RequestBody VeterinaryVisit visit) {
        // Verify that the dog exists
        if (visit.getDog() == null || visit.getDog().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<Dog> optionalDog = dogDao.findById(visit.getDog().getId());
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Set the dog to ensure the relationship is properly established
        visit.setDog(optionalDog.get());
        
        veterinaryVisitDao.save(visit);
        return new ResponseEntity<>(visit, HttpStatus.CREATED);
    }

    @DeleteMapping("/visit/{id}")
    public ResponseEntity<VeterinaryVisit> deleteVisit(@PathVariable Long id) {
        Optional<VeterinaryVisit> optionalVisit = veterinaryVisitDao.findById(id);
        if (optionalVisit.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        veterinaryVisitDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/visit/{id}")
    public ResponseEntity<VeterinaryVisit> updateVisit(@PathVariable Long id, @RequestBody VeterinaryVisit visit) {
        Optional<VeterinaryVisit> optionalVisit = veterinaryVisitDao.findById(id);
        if (optionalVisit.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the dog exists if it's being updated
        if (visit.getDog() != null && visit.getDog().getId() != null) {
            Optional<Dog> optionalDog = dogDao.findById(visit.getDog().getId());
            if (optionalDog.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            visit.setDog(optionalDog.get());
        } else {
            // Keep the existing dog if not provided in the update
            visit.setDog(optionalVisit.get().getDog());
        }
        
        visit.setId(id);
        veterinaryVisitDao.save(visit);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}