package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.dao.BreedDao;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class BreedController {

    protected BreedDao breedDao;

    @Autowired
    public BreedController(BreedDao breedDao) {
        this.breedDao = breedDao;
    }

    @GetMapping("/breed/{id}")
    public ResponseEntity<Breed> getBreed(@PathVariable Short id) {
        Optional<Breed> optionalBreed = breedDao.findByIdWithDogs(id);
        if (optionalBreed.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalBreed.get(), HttpStatus.OK);
    }

    @IsOwner
    @GetMapping("/breeds")
    @JsonView(OwnerViewDog.class)
    public List<Breed> getAll() {
        return breedDao.findAllWithDogs();
    }

    @PostMapping("/breed")
    public ResponseEntity<Breed> createBreed(@RequestBody Breed breed) {
        breedDao.save(breed);
        return new ResponseEntity<>(breed, HttpStatus.CREATED);
    }

    @DeleteMapping("/breed/{id}")
    public ResponseEntity<Breed> deleteBreed(@PathVariable Short id) {
        Optional<Breed> optionalBreed = breedDao.findById(id);
        if (optionalBreed.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        breedDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/breed/{id}")
    public ResponseEntity<Breed> updateBreed(@PathVariable Short id, @RequestBody Breed breed) {
        Optional<Breed> optionalBreed = breedDao.findById(id);
        if (optionalBreed.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        breed.setId(id);
        breedDao.save(breed);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
