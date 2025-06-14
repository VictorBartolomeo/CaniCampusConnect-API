package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.dao.AgeRangeDao;
import org.example.canicampusconnectapi.dao.CourseTypeDao;
import org.example.canicampusconnectapi.model.courseRelated.AgeRange;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.view.utilities.CourseTypeView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@IsClubOwner
public class CourseTypeController {

    protected CourseTypeDao courseTypeDao;
    protected AgeRangeDao ageRangeDao;

    @Autowired
    public CourseTypeController(CourseTypeDao courseTypeDao, AgeRangeDao ageRangeDao) {
        this.courseTypeDao = courseTypeDao;
        this.ageRangeDao = ageRangeDao;
    }

    @IsOwner
    @JsonView(CourseTypeView.class)
    @GetMapping("/coursetype/{id}")
    public ResponseEntity<CourseType> getCourseType(@PathVariable Long id) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(id);
        return optionalCourseType
                .map(courseType -> new ResponseEntity<>(courseType, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @JsonView(CourseTypeView.class)
    @GetMapping("/coursetypes")
    public List<CourseType> getAllCourseTypes() {
        return courseTypeDao.findAll();
    }

    @JsonView(CourseTypeView.class)
    @GetMapping("/agerange/{ageRangeId}/coursetypes")
    public ResponseEntity<List<CourseType>> getCourseTypesByAgeRange(@PathVariable Long ageRangeId) {
        Optional<AgeRange> optionalAgeRange = ageRangeDao.findById(ageRangeId);
        if (optionalAgeRange.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CourseType> courseTypes = courseTypeDao.findByAgeRange(optionalAgeRange.get());
        return new ResponseEntity<>(courseTypes, HttpStatus.OK);
    }

    @JsonView(CourseTypeView.class)
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

    @JsonView(CourseTypeView.class)
    @DeleteMapping("/coursetype/{id}")
    public ResponseEntity<?> deleteCourseType(@PathVariable Long id) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(id);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CourseType courseType = optionalCourseType.get();

        if (courseType.getCourses() != null && !courseType.getCourses().isEmpty()) {
            List<String> courseTitles = courseType.getCourses().stream()
                    .map(Course::getTitle)
                    .collect(Collectors.toList());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Impossible de supprimer ce type de cours car il est utilisé par " +
                    courseType.getCourses().size() + " cours. Vous devez d'abord supprimer ou modifier ces cours.");
            errorResponse.put("affectedCourses", courseTitles);
            errorResponse.put("courseTypeId", id);
            errorResponse.put("courseTypeName", courseType.getName());
            errorResponse.put("suggestedEndpoint", "/coursetype/" + id + "/affected-courses");
            errorResponse.put("timestamp", new Date());
            errorResponse.put("status", 409);
            errorResponse.put("error", "Conflict");
            errorResponse.put("path", "/coursetype/" + id);

            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        courseTypeDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @JsonView(CourseTypeView.class)
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

    @JsonView(CourseTypeView.class)
    @GetMapping("/coursetype/{id}/affected-courses")
    public ResponseEntity<List<Course>> getCoursesByType(@PathVariable Long id) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(id);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        CourseType courseType = optionalCourseType.get();
        List<Course> courses = courseType.getCourses();

        return new ResponseEntity<>(courses != null ? courses : List.of(), HttpStatus.OK);
    }
}