package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.dao.AgeRangeDao;
import org.example.canicampusconnectapi.model.courseRelated.AgeRange;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.view.utilities.AgeRangeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@IsClubOwner
public class AgeRangeController {

    protected AgeRangeDao ageRangeDao;

    @Autowired
    public AgeRangeController(AgeRangeDao ageRangeDao) {
        this.ageRangeDao = ageRangeDao;
    }

    @JsonView(AgeRangeView.class)
    @GetMapping("/agerange/{id}")
    public ResponseEntity<AgeRange> getAgeRange(@PathVariable Long id) {
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(id);
        return optionalAgeRange
                .map(ageRange -> new ResponseEntity<>(ageRange, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @JsonView(AgeRangeView.class)
    @GetMapping("/ageranges")
    public List<AgeRange> getAllAgeRanges() {
        return ageRangeDao.findAll();
    }

    @JsonView(AgeRangeView.class)
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

        AgeRange ageRange = optionalAgeRange.get();
        if (ageRange.getCourseTypes() != null && !ageRange.getCourseTypes().isEmpty()) {
            String courseTypeNames = ageRange.getCourseTypes().stream()
                    .map(CourseType::getName)
                    .collect(Collectors.joining(", "));

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Impossible de supprimer cette tranche d'âge car elle est utilisée par les types de cours suivants : " + courseTypeNames
            );
        }

        ageRangeDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @JsonView(AgeRangeView.class)
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

    @JsonView(AgeRangeView.class)
    @GetMapping("/agerange/{id}/affected-coursetypes")
    public ResponseEntity<List<CourseType>> getCourseTypesByAgeRange(@PathVariable Long id) {
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(id);
        return optionalAgeRange
                .map(ageRange -> new ResponseEntity<>(ageRange.getCourseTypes(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}