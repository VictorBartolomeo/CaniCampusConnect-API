package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.DogWeightDao;
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.model.DogWeight;
import org.example.canicampusconnectapi.model.Dog;
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
public class DogWeightController {

    protected DogWeightDao dogWeightDao;
    protected DogDao dogDao;

    @Autowired
    public DogWeightController(DogWeightDao dogWeightDao, DogDao dogDao) {
        this.dogWeightDao = dogWeightDao;
        this.dogDao = dogDao;
    }

    @GetMapping("/dogweight/{id}")
    public ResponseEntity<DogWeight> getDogWeight(@PathVariable Long id) {
        Optional<DogWeight> optionalDogWeight = dogWeightDao.findById(id);
        if (optionalDogWeight.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDogWeight.get(), HttpStatus.OK);
    }

    @GetMapping("/dogweights")
    public List<DogWeight> getAllDogWeights() {
        return dogWeightDao.findAll();
    }

    @GetMapping("/dog/{dogId}/weights")
    public ResponseEntity<List<DogWeight>> getDogWeightsByDog(@PathVariable Long dogId) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<DogWeight> dogWeights = dogWeightDao.findByDogOrderByMeasurementDateDesc(optionalDog.get());
        return new ResponseEntity<>(dogWeights, HttpStatus.OK);
    }

    @GetMapping("/dogweights/between")
    public List<DogWeight> getDogWeightsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return dogWeightDao.findByMeasurementDateBetween(startDate, endDate);
    }

    @GetMapping("/dog/{dogId}/weights/between")
    public ResponseEntity<List<DogWeight>> getDogWeightsByDogBetweenDates(
            @PathVariable Long dogId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<DogWeight> dogWeights = dogWeightDao.findByDogAndMeasurementDateBetween(optionalDog.get(), startDate, endDate);
        return new ResponseEntity<>(dogWeights, HttpStatus.OK);
    }

    @PostMapping("/dogweight")
    public ResponseEntity<DogWeight> createDogWeight(@RequestBody DogWeight dogWeight) {
        // Verify that the dog exists
        if (dogWeight.getDog() == null || dogWeight.getDog().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<Dog> optionalDog = dogDao.findById(dogWeight.getDog().getId());
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Set the dog to ensure the relationship is properly established
        dogWeight.setDog(optionalDog.get());
        
        dogWeightDao.save(dogWeight);
        return new ResponseEntity<>(dogWeight, HttpStatus.CREATED);
    }

    @DeleteMapping("/dogweight/{id}")
    public ResponseEntity<DogWeight> deleteDogWeight(@PathVariable Long id) {
        Optional<DogWeight> optionalDogWeight = dogWeightDao.findById(id);
        if (optionalDogWeight.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dogWeightDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/dogweight/{id}")
    public ResponseEntity<DogWeight> updateDogWeight(@PathVariable Long id, @RequestBody DogWeight dogWeight) {
        Optional<DogWeight> optionalDogWeight = dogWeightDao.findById(id);
        if (optionalDogWeight.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the dog exists if it's being updated
        if (dogWeight.getDog() != null && dogWeight.getDog().getId() != null) {
            Optional<Dog> optionalDog = dogDao.findById(dogWeight.getDog().getId());
            if (optionalDog.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            dogWeight.setDog(optionalDog.get());
        } else {
            // Keep the existing dog if not provided in the update
            dogWeight.setDog(optionalDogWeight.get().getDog());
        }
        
        dogWeight.setId(id);
        dogWeightDao.save(dogWeight);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}