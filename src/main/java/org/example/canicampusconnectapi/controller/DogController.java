package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.model.Dog;
import org.example.canicampusconnectapi.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class DogController {

    protected DogDao dogDao;
    protected OwnerDao ownerDao;

    @Autowired
    public DogController(DogDao dogDao, OwnerDao ownerDao) {
        this.dogDao = dogDao;
        this.ownerDao = ownerDao;
    }

    @GetMapping("/dog/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        Optional<Dog> optionalDog = dogDao.findById(id);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDog.get(), HttpStatus.OK);
    }

    @GetMapping("/dogs")
    public List<Dog> getAllDogs() {
        return dogDao.findAll();
    }

    @GetMapping("/dogs/name/{name}")
    public List<Dog> getDogsByName(@PathVariable String name) {
        return dogDao.findByName(name);
    }

    @GetMapping("/dog/chip/{chipNumber}")
    public ResponseEntity<Dog> getDogByChipNumber(@PathVariable String chipNumber) {
        Optional<Dog> optionalDog = dogDao.findByChipNumber(chipNumber);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDog.get(), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}/dogs")
    public ResponseEntity<List<Dog>> getDogsByOwner(@PathVariable Long ownerId) {
        Optional<Owner> optionalOwner = ownerDao.findById(ownerId);
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Dog> dogs = dogDao.findByOwner(optionalOwner.get());
        return new ResponseEntity<>(dogs, HttpStatus.OK);
    }

    @PostMapping("/dog")
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        // Verify that the owner exists
        if (dog.getOwner() == null || dog.getOwner().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<Owner> optionalOwner = ownerDao.findById(dog.getOwner().getId());
        if (optionalOwner.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Set the owner to ensure the relationship is properly established
        dog.setOwner(optionalOwner.get());
        
        dogDao.save(dog);
        return new ResponseEntity<>(dog, HttpStatus.CREATED);
    }

    @DeleteMapping("/dog/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable Long id) {
        Optional<Dog> optionalDog = dogDao.findById(id);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        dogDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/dog/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @RequestBody Dog dog) {
        Optional<Dog> optionalDog = dogDao.findById(id);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the owner exists if it's being updated
        if (dog.getOwner() != null && dog.getOwner().getId() != null) {
            Optional<Owner> optionalOwner = ownerDao.findById(dog.getOwner().getId());
            if (optionalOwner.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            dog.setOwner(optionalOwner.get());
        } else {
            // Keep the existing owner if not provided in the update
            dog.setOwner(optionalDog.get().getOwner());
        }
        
        dog.setId(id);
        dogDao.save(dog);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}