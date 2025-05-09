package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.VaccinationDao;
import org.example.canicampusconnectapi.dao.VaccineDao;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.Vaccination;
import org.example.canicampusconnectapi.model.healthRecord.Vaccine;
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
public class VaccinationController {

    protected VaccinationDao vaccinationDao;
    protected DogDao dogDao;
    protected VaccineDao vaccineDao;

    @Autowired
    public VaccinationController(VaccinationDao vaccinationDao, DogDao dogDao, VaccineDao vaccineDao) {
        this.vaccinationDao = vaccinationDao;
        this.dogDao = dogDao;
        this.vaccineDao = vaccineDao;
    }

    @GetMapping("/vaccination/{id}")
    public ResponseEntity<Vaccination> getVaccination(@PathVariable Long id) {
        Optional<Vaccination> optionalVaccination = vaccinationDao.findById(id);
        if (optionalVaccination.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalVaccination.get(), HttpStatus.OK);
    }

    @GetMapping("/vaccinations")
    public List<Vaccination> getAllVaccinations() {
        return vaccinationDao.findAll();
    }

    @GetMapping("/dog/{dogId}/vaccinations")
    public ResponseEntity<List<Vaccination>> getVaccinationsByDog(@PathVariable Long dogId) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Vaccination> vaccinations = vaccinationDao.findByDog(optionalDog.get());
        return new ResponseEntity<>(vaccinations, HttpStatus.OK);
    }

    @GetMapping("/vaccine/{vaccineId}/vaccinations")
    public ResponseEntity<List<Vaccination>> getVaccinationsByVaccine(@PathVariable Integer vaccineId) {
        Optional<Vaccine> optionalVaccine = vaccineDao.findById(vaccineId);
        if (optionalVaccine.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Vaccination> vaccinations = vaccinationDao.findByVaccine(optionalVaccine.get());
        return new ResponseEntity<>(vaccinations, HttpStatus.OK);
    }

    @GetMapping("/dog/{dogId}/vaccine/{vaccineId}/vaccinations")
    public ResponseEntity<List<Vaccination>> getVaccinationsByDogAndVaccine(
            @PathVariable Long dogId, @PathVariable Integer vaccineId) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Vaccine> optionalVaccine = vaccineDao.findById(vaccineId);
        if (optionalVaccine.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Vaccination> vaccinations = vaccinationDao.findByDogAndVaccine(optionalDog.get(), optionalVaccine.get());
        return new ResponseEntity<>(vaccinations, HttpStatus.OK);
    }

    @GetMapping("/vaccinations/between")
    public List<Vaccination> getVaccinationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return vaccinationDao.findByVaccinationDateBetween(startDate, endDate);
    }



    @PostMapping("/vaccination")
    public ResponseEntity<Vaccination> createVaccination(@RequestBody Vaccination vaccination) {
        // Verify that the dog exists
        if (vaccination.getDog() == null || vaccination.getDog().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Dog> optionalDog = dogDao.findById(vaccination.getDog().getId());
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the vaccine exists
        if (vaccination.getVaccine() == null || vaccination.getVaccine().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Vaccine> optionalVaccine = vaccineDao.findById(vaccination.getVaccine().getId());
        if (optionalVaccine.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Set the dog and vaccine to ensure the relationships are properly established
        vaccination.setDog(optionalDog.get());
        vaccination.setVaccine(optionalVaccine.get());
        
        vaccinationDao.save(vaccination);
        return new ResponseEntity<>(vaccination, HttpStatus.CREATED);
    }

    @DeleteMapping("/vaccination/{id}")
    public ResponseEntity<Vaccination> deleteVaccination(@PathVariable Long id) {
        Optional<Vaccination> optionalVaccination = vaccinationDao.findById(id);
        if (optionalVaccination.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vaccinationDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/vaccination/{id}")
    public ResponseEntity<Vaccination> updateVaccination(@PathVariable Long id, @RequestBody Vaccination vaccination) {
        Optional<Vaccination> optionalVaccination = vaccinationDao.findById(id);
        if (optionalVaccination.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the dog exists if it's being updated
        if (vaccination.getDog() != null && vaccination.getDog().getId() != null) {
            Optional<Dog> optionalDog = dogDao.findById(vaccination.getDog().getId());
            if (optionalDog.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            vaccination.setDog(optionalDog.get());
        } else {
            // Keep the existing dog if not provided in the update
            vaccination.setDog(optionalVaccination.get().getDog());
        }
        
        // Verify that the vaccine exists if it's being updated
        if (vaccination.getVaccine() != null && vaccination.getVaccine().getId() != null) {
            Optional<Vaccine> optionalVaccine = vaccineDao.findById(vaccination.getVaccine().getId());
            if (optionalVaccine.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            vaccination.setVaccine(optionalVaccine.get());
        } else {
            // Keep the existing vaccine if not provided in the update
            vaccination.setVaccine(optionalVaccination.get().getVaccine());
        }
        
        vaccination.setId(id);
        vaccinationDao.save(vaccination);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}