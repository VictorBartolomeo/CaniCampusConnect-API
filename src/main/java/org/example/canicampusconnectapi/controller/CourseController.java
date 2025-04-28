package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.ClubDao;
import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.CourseTypeDao;
import org.example.canicampusconnectapi.model.Club;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class CourseController {

    protected CourseDao courseDao;
    protected CoachDao coachDao;
    protected ClubDao clubDao;
    protected CourseTypeDao courseTypeDao;

    @Autowired
    public CourseController(CourseDao courseDao, CoachDao coachDao, ClubDao clubDao, CourseTypeDao courseTypeDao) {
        this.courseDao = courseDao;
        this.coachDao = coachDao;
        this.clubDao = clubDao;
        this.courseTypeDao = courseTypeDao;
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
    public List<Course> getAllCourses() {
        // Get the default club (ID 1)
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isPresent()) {
            return courseDao.findByClub(defaultClub.get());
        }
        return List.of(); // Return empty list if default club not found
    }

    @GetMapping("/coach/{coachId}/courses")
    public ResponseEntity<List<Course>> getCoursesByCoach(@PathVariable Long coachId) {
        Optional<Coach> optionalCoach = coachDao.findById(coachId);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Get the default club (ID 1)
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK); // Return empty list if default club not found
        }

        List<Course> courses = courseDao.findByClubAndCoach(defaultClub.get(), optionalCoach.get());
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/club/{clubId}/courses")
    public ResponseEntity<List<Course>> getCoursesByClub(@PathVariable Integer clubId) {
        // Always use the default club (ID 1), regardless of the clubId provided
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Course> courses = courseDao.findByClub(defaultClub.get());
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/coursetype/{courseTypeId}/courses")
    public ResponseEntity<List<Course>> getCoursesByCourseType(@PathVariable Long courseTypeId) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(courseTypeId);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Get the default club (ID 1)
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK); // Return empty list if default club not found
        }

        List<Course> courses = courseDao.findByClubAndCourseType(defaultClub.get(), optionalCourseType.get());
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/courses/upcoming")
    public List<Course> getUpcomingCourses() {
        // Get the default club (ID 1)
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isPresent()) {
            return courseDao.findByClubAndStartDatetimeAfter(defaultClub.get(), LocalDateTime.now());
        }
        return List.of(); // Return empty list if default club not found
    }

    @GetMapping("/courses/between")
    public List<Course> getCoursesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        // Get the default club (ID 1)
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isPresent()) {
            return courseDao.findByClubAndStartDatetimeBetween(defaultClub.get(), start, end);
        }
        return List.of(); // Return empty list if default club not found
    }

    @PostMapping("/course")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        // Verify that the coach exists
        if (course.getCoach() == null || course.getCoach().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Coach> optionalCoach = coachDao.findById(course.getCoach().getId());
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Always use the default club (ID 1)
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Verify that the course type exists
        if (course.getCourseType() == null || course.getCourseType().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(course.getCourseType().getId());
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Set the entities with the full objects from the database
        course.setCoach(optionalCoach.get());
        course.setClub(defaultClub.get()); // Always use the default club
        course.setCourseType(optionalCourseType.get());

        courseDao.save(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        Optional<Course> optionalCourse = courseDao.findById(id);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        courseDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Optional<Course> optionalCourse = courseDao.findById(id);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Verify that the coach exists if it's being updated
        if (course.getCoach() != null && course.getCoach().getId() != null) {
            Optional<Coach> optionalCoach = coachDao.findById(course.getCoach().getId());
            if (optionalCoach.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            course.setCoach(optionalCoach.get());
        } else {
            // Keep the existing coach if not provided
            course.setCoach(optionalCourse.get().getCoach());
        }

        // Always use the default club (ID 1)
        Optional<Club> defaultClub = clubDao.findById(1);
        if (defaultClub.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        course.setClub(defaultClub.get());

        // Verify that the course type exists if it's being updated
        if (course.getCourseType() != null && course.getCourseType().getId() != null) {
            Optional<CourseType> optionalCourseType = courseTypeDao.findById(course.getCourseType().getId());
            if (optionalCourseType.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            course.setCourseType(optionalCourseType.get());
        } else {
            // Keep the existing course type if not provided
            course.setCourseType(optionalCourse.get().getCourseType());
        }

        course.setId(id);
        courseDao.save(course);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
