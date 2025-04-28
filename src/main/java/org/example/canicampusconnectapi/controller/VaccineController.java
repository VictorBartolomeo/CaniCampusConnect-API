package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.VaccineDao;
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
public class VaccineController {

    protected VaccineDao vaccineDao;

    @Autowired
    public VaccineController(VaccineDao vaccineDao) {
        this.vaccineDao = vaccineDao;
    }

    @GetMapping("/vaccine/{id}")
    public ResponseEntity<Vaccine> getVaccine(@PathVariable Integer id) {
        Optional<Vaccine> optionalVaccine = vaccineDao.findById(id);
        if (optionalVaccine.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalVaccine.get(), HttpStatus.OK);
    }

    @GetMapping("/vaccines")
    public List<Vaccine> getAllVaccines() {
        return vaccineDao.findAll();
    }

    @GetMapping("/vaccine/name/{name}")
    public ResponseEntity<Vaccine> getVaccineByName(@PathVariable String name) {
        Optional<Vaccine> optionalVaccine = vaccineDao.findByName(name);
        if (optionalVaccine.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalVaccine.get(), HttpStatus.OK);
    }

    @GetMapping("/vaccines/renew-before")
    public List<Vaccine> getVaccinesRenewBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return vaccineDao.findByRenewDelayLessThan(date);
    }

    @GetMapping("/vaccines/renew-after")
    public List<Vaccine> getVaccinesRenewAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return vaccineDao.findByRenewDelayGreaterThan(date);
    }

    @PostMapping("/vaccine")
    public ResponseEntity<Vaccine> createVaccine(@RequestBody Vaccine vaccine) {
        vaccineDao.save(vaccine);
        return new ResponseEntity<>(vaccine, HttpStatus.CREATED);
    }

    @DeleteMapping("/vaccine/{id}")
    public ResponseEntity<Vaccine> deleteVaccine(@PathVariable Integer id) {
        Optional<Vaccine> optionalVaccine = vaccineDao.findById(id);
        if (optionalVaccine.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vaccineDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/vaccine/{id}")
    public ResponseEntity<Vaccine> updateVaccine(@PathVariable Integer id, @RequestBody Vaccine vaccine) {
        Optional<Vaccine> optionalVaccine = vaccineDao.findById(id);
        if (optionalVaccine.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        vaccine.setId(id);
        vaccineDao.save(vaccine);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}