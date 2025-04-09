package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class CourseController {

    protected CourseDao courseDao;

    @Autowired
    public CourseController(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @GetMapping("/course/{id}")

    public ResponseEntity<Course> getCourse(@PathVariable Long id) {

        Optional<Course> optionalCourse = courseDao.findById(id);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalCourse.get(), HttpStatus.OK);

    }


    @GetMapping("/courses")
    public List<Course> getAll() {
        return courseDao.findAll();
    }

    @PostMapping("/course")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        courseDao.save(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @DeleteMapping("course/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable Long id) {

        Optional<Course> optionalCourse = courseDao.findById(id);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        courseDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Put change tout l'objet
    @PutMapping("/course/{id}")
    // Patch change une partie de l'objet
//    @PatchMapping("/course/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Optional<Course> optionalCourse = courseDao.findById(id);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        course.setId(id);
        courseDao.save(course);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
