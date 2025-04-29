package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.service.dogweight.DogWeightService;
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

    private final DogWeightService dogWeightService;

    @Autowired
    public DogWeightController(DogWeightService dogWeightService) {
        this.dogWeightService = dogWeightService;
    }

    @GetMapping("/dogweight/{id}")
    public ResponseEntity<DogWeight> getDogWeight(@PathVariable Long id) {
        Optional<DogWeight> optionalDogWeight = dogWeightService.getDogWeightById(id);
        if (optionalDogWeight.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDogWeight.get(), HttpStatus.OK);
    }

    @GetMapping("/dogweights")
    public List<DogWeight> getAllDogWeights() {
        return dogWeightService.getAllDogWeights();
    }

    @GetMapping("/dog/{dogId}/weights")
    public ResponseEntity<List<DogWeight>> getDogWeightsByDog(@PathVariable Long dogId) {
        try {
            List<DogWeight> dogWeights = dogWeightService.getDogWeightsByDog(dogId);
            return new ResponseEntity<>(dogWeights, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dogweights/between")
    public List<DogWeight> getDogWeightsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return dogWeightService.getDogWeightsBetweenDates(startDate, endDate);
    }

    @GetMapping("/dog/{dogId}/weights/between")
    public ResponseEntity<List<DogWeight>> getDogWeightsByDogBetweenDates(
            @PathVariable Long dogId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            List<DogWeight> dogWeights = dogWeightService.getDogWeightsByDogBetweenDates(dogId, startDate, endDate);
            return new ResponseEntity<>(dogWeights, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/dogweight")
    public ResponseEntity<DogWeight> createDogWeight(@RequestBody DogWeight dogWeight) {
        try {
            DogWeight createdDogWeight = dogWeightService.createDogWeight(dogWeight);
            return new ResponseEntity<>(createdDogWeight, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/dogweight/{id}")
    public ResponseEntity<DogWeight> deleteDogWeight(@PathVariable Long id) {
        try {
            dogWeightService.deleteDogWeight(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/dogweight/{id}")
    public ResponseEntity<DogWeight> updateDogWeight(@PathVariable Long id, @RequestBody DogWeight dogWeight) {
        try {
            DogWeight updatedDogWeight = dogWeightService.updateDogWeight(id, dogWeight);
            return new ResponseEntity<>(updatedDogWeight, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dog/{dogId}/weights/last7days")
    public ResponseEntity<List<DogWeight>> getDogWeightsFromLast7Days(@PathVariable Long dogId) {
        try {
            List<DogWeight> dogWeights = dogWeightService.getDogWeightsFromLast7Days(dogId);
            return new ResponseEntity<>(dogWeights, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dog/{dogId}/weights/last3months")
    public ResponseEntity<List<DogWeight>> getDogWeightsFromLast3Months(@PathVariable Long dogId) {
        try {
            List<DogWeight> dogWeights = dogWeightService.getDogWeightsFromLast3Months(dogId);
            return new ResponseEntity<>(dogWeights, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dog/{dogId}/weights/last6months")
    public ResponseEntity<List<DogWeight>> getDogWeightsFromLast6Months(@PathVariable Long dogId) {
        try {
            List<DogWeight> dogWeights = dogWeightService.getDogWeightsFromLast6Months(dogId);
            return new ResponseEntity<>(dogWeights, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dog/{dogId}/weights/last12months")
    public ResponseEntity<List<DogWeight>> getDogWeightsFromLast12Months(@PathVariable Long dogId) {
        try {
            List<DogWeight> dogWeights = dogWeightService.getDogWeightsFromLast12Months(dogId);
            return new ResponseEntity<>(dogWeights, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
