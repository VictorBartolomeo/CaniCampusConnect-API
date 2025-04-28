package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.AgeRangeDao;
import org.example.canicampusconnectapi.model.AgeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class AgeRangeController {

    protected AgeRangeDao ageRangeDao;

    @Autowired
    public AgeRangeController(AgeRangeDao ageRangeDao) {
        this.ageRangeDao = ageRangeDao;
    }

    @GetMapping("/agerange/{id}")
    public ResponseEntity<AgeRange> getAgeRange(@PathVariable Long id) {
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(id);
        if (optionalAgeRange.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalAgeRange.get(), HttpStatus.OK);
    }

    @GetMapping("/ageranges")
    public List<AgeRange> getAllAgeRanges() {
        return ageRangeDao.findAll();
    }

    @GetMapping("/ageranges/for-age/{age}")
    public List<AgeRange> getAgeRangesForAge(@PathVariable int age) {
        return ageRangeDao.findByMinAgeLessThanEqualAndMaxAgeGreaterThanEqual(age, age);
    }

    @GetMapping("/ageranges/older-than/{minAge}")
    public List<AgeRange> getAgeRangesForOlderDogs(@PathVariable int minAge) {
        return ageRangeDao.findByMinAgeGreaterThanEqual(minAge);
    }

    @GetMapping("/ageranges/younger-than/{maxAge}")
    public List<AgeRange> getAgeRangesForYoungerDogs(@PathVariable int maxAge) {
        return ageRangeDao.findByMaxAgeLessThanEqual(maxAge);
    }

    @PostMapping("/agerange")
    public ResponseEntity<AgeRange> createAgeRange(@RequestBody AgeRange ageRange) {
        // Check if an age range with the same min and max age already exists
        Optional<AgeRange> existingAgeRange = ageRangeDao.findByMinAgeAndMaxAge(ageRange.getMinAge(), ageRange.getMaxAge());
        if (existingAgeRange.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        // Validate that min age is less than or equal to max age
        if (ageRange.getMinAge() > ageRange.getMaxAge()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        ageRangeDao.save(ageRange);
        return new ResponseEntity<>(ageRange, HttpStatus.CREATED);
    }

    @DeleteMapping("/agerange/{id}")
    public ResponseEntity<Void> deleteAgeRange(@PathVariable Long id) {
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(id);
        if (optionalAgeRange.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Check if the age range is used by any course types
        AgeRange ageRange = optionalAgeRange.get();
        if (ageRange.getCourseTypes() != null && !ageRange.getCourseTypes().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        ageRangeDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/agerange/{id}")
    public ResponseEntity<Void> updateAgeRange(@PathVariable Long id, @RequestBody AgeRange ageRange) {
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(id);
        if (optionalAgeRange.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Validate that min age is less than or equal to max age
        if (ageRange.getMinAge() > ageRange.getMaxAge()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        // Check if another age range with the same min and max age already exists
        Optional<AgeRange> existingAgeRange = ageRangeDao.findByMinAgeAndMaxAge(ageRange.getMinAge(), ageRange.getMaxAge());
        if (existingAgeRange.isPresent() && !existingAgeRange.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        ageRange.setId(id);
        ageRangeDao.save(ageRange);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}