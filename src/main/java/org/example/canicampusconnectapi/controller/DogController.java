package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.EntityNotFoundException;
import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.dto.DogDashboardDTO;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.dog.DogService;
import org.example.canicampusconnectapi.service.dogweight.DogWeightService;
import org.example.canicampusconnectapi.service.veterinary.VeterinaryVisitService;
import org.example.canicampusconnectapi.view.admin.AdminViewDog;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
public class DogController {

    private final DogService dogService;


    @Autowired
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }


    @IsOwner
    @GetMapping("/dog/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        Optional<Dog> optionalDog = dogService.getDogById(id);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDog.get(), HttpStatus.OK);
    }

    @IsClubOwner
    @GetMapping("/dogs")
    @JsonView(AdminViewDog.class)
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }


    @IsOwner
    //TODO Demander à Franck pourquoi le role est bloquant alors que je l'ai bien dans le jwt
    @GetMapping("/owner/{ownerId}/dogs")
    // TODO Demander pourquoi j'ai un JSON infini alors que j'ai des JsonView sur les éléments
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<List<Dog>> getDogsByOwner(@PathVariable Long ownerId) {
        try {
            List<Dog> dogs = dogService.getDogsByOwner(ownerId);
            return new ResponseEntity<>(dogs, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @IsOwner
    @GetMapping("/owner/{ownerId}/dog/{dogId}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> getDogByOwnerAndDogId(@PathVariable Long ownerId, @PathVariable Long dogId) {
        try {
            Dog dog = dogService.getDogByOwnerIdAndDogId(ownerId, dogId);
            return new ResponseEntity<>(dog, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @IsOwner
    @PostMapping("/dog")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        try {
            Dog createdDog = dogService.createDog(dog);
            return new ResponseEntity<>(createdDog, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @IsClubOwner
    @DeleteMapping("/dog/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable Long id) {
        try {
            dogService.deleteDog(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @IsOwner
    @PutMapping("/dog/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @RequestBody Dog dog) {
        try {
            Dog updatedDog = dogService.updateDog(id, dog);
            return new ResponseEntity<>(updatedDog, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
