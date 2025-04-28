package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.AgeRangeDao;
import org.example.canicampusconnectapi.dao.CourseTypeDao;
import org.example.canicampusconnectapi.model.courseRelated.AgeRange;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class CourseTypeController {

    protected CourseTypeDao courseTypeDao;
    protected AgeRangeDao ageRangeDao;

    @Autowired
    public CourseTypeController(CourseTypeDao courseTypeDao, AgeRangeDao ageRangeDao) {
        this.courseTypeDao = courseTypeDao;
        this.ageRangeDao = ageRangeDao;
    }

    @GetMapping("/coursetype/{id}")
    public ResponseEntity<CourseType> getCourseType(@PathVariable Long id) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(id);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalCourseType.get(), HttpStatus.OK);
    }

    @GetMapping("/coursetypes")
    public List<CourseType> getAllCourseTypes() {
        return courseTypeDao.findAll();
    }

    @GetMapping("/agerange/{ageRangeId}/coursetypes")
    public ResponseEntity<List<CourseType>> getCourseTypesByAgeRange(@PathVariable Long ageRangeId) {
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(ageRangeId);
        if (optionalAgeRange.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CourseType> courseTypes = courseTypeDao.findByAgeRange(optionalAgeRange.get());
        return new ResponseEntity<>(courseTypes, HttpStatus.OK);
    }

    @PostMapping("/coursetype")
    public ResponseEntity<CourseType> createCourseType(@RequestBody CourseType courseType) {
        // Verify that the age range exists
        if (courseType.getAgeRange() == null || courseType.getAgeRange().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(courseType.getAgeRange().getId());
        if (optionalAgeRange.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        courseType.setAgeRange(optionalAgeRange.get());
        courseTypeDao.save(courseType);
        return new ResponseEntity<>(courseType, HttpStatus.CREATED);
    }

    @DeleteMapping("/coursetype/{id}")
    public ResponseEntity<Void> deleteCourseType(@PathVariable Long id) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(id);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        courseTypeDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/coursetype/{id}")
    public ResponseEntity<Void> updateCourseType(@PathVariable Long id, @RequestBody CourseType courseType) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(id);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Verify that the age range exists if it's being updated
        if (courseType.getAgeRange() != null && courseType.getAgeRange().getId() != null) {
            Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(courseType.getAgeRange().getId());
            if (optionalAgeRange.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            courseType.setAgeRange(optionalAgeRange.get());
        } else {
            // Keep the existing age range if not provided
            courseType.setAgeRange(optionalCourseType.get().getAgeRange());
        }
        
        courseType.setId(id);
        courseTypeDao.save(courseType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}