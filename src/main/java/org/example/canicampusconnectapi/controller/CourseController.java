package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.ClubDao;
import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.CourseTypeDao;
import org.example.canicampusconnectapi.model.Club;
import org.example.canicampusconnectapi.model.Coach;
import org.example.canicampusconnectapi.model.Course;
import org.example.canicampusconnectapi.model.CourseType;
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
        return courseDao.findAll();
    }

    @GetMapping("/coach/{coachId}/courses")
    public ResponseEntity<List<Course>> getCoursesByCoach(@PathVariable Long coachId) {
        Optional<Coach> optionalCoach = coachDao.findById(coachId);
        if (optionalCoach.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Course> courses = courseDao.findByCoach(optionalCoach.get());
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/club/{clubId}/courses")
    public ResponseEntity<List<Course>> getCoursesByClub(@PathVariable Integer clubId) {
        Optional<Club> optionalClub = clubDao.findById(clubId);
        if (optionalClub.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Course> courses = courseDao.findByClub(optionalClub.get());
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/coursetype/{courseTypeId}/courses")
    public ResponseEntity<List<Course>> getCoursesByCourseType(@PathVariable Long courseTypeId) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(courseTypeId);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Course> courses = courseDao.findByCourseType(optionalCourseType.get());
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/courses/upcoming")
    public List<Course> getUpcomingCourses() {
        return courseDao.findByStartDatetimeAfter(LocalDateTime.now());
    }

    @GetMapping("/courses/between")
    public List<Course> getCoursesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return courseDao.findByStartDatetimeBetween(start, end);
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

        // Verify that the club exists
        if (course.getClub() == null || course.getClub().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Club> optionalClub = clubDao.findById(course.getClub().getId());
        if (optionalClub.isEmpty()) {
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
        course.setClub(optionalClub.get());
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

        // Verify that the club exists if it's being updated
        if (course.getClub() != null && course.getClub().getId() != null) {
            Optional<Club> optionalClub = clubDao.findById(course.getClub().getId());
            if (optionalClub.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            course.setClub(optionalClub.get());
        } else {
            // Keep the existing club if not provided
            course.setClub(optionalCourse.get().getClub());
        }

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
