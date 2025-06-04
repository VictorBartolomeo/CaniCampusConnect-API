package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.course.CourseService;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @IsOwner
    @JsonView({OwnerViewDog.class})
    @GetMapping("/course/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course.get(), HttpStatus.OK);
    }

    @IsOwner
    @JsonView({OwnerViewCourse.class})
    @GetMapping("/course/{id}/owner")
    public ResponseEntity<Course> getCourseByOwner(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course.get(), HttpStatus.OK);
    }

    @IsOwner
@JsonView(OwnerViewDog.class)
    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
    @IsOwner
    @JsonView(OwnerViewCourse.class)
    @GetMapping("/courses/owner")
    public List<Course> getAllCoursesForOwner() {
        return courseService.getAllCourses();
    }

    @JsonView({CoachView.class})
    @GetMapping("/coach/{coachId}/courses/all")
    public ResponseEntity<List<Course>> getAllCoursesByCoach(@PathVariable Long coachId) {
        try {
            List<Course> courses = courseService.getCoursesByCoach(coachId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Nouvelle méthode pour les cours à venir d'un coach
    @IsCoach
    @JsonView({CoachView.class})
    @GetMapping("/coach/{coachId}/courses/upcoming")
    public ResponseEntity<List<Course>> getUpcomingCoursesByCoach(@PathVariable Long coachId) {
        try {
            List<Course> upcomingCourses = courseService.getUpcomingCoursesByCoach(coachId);
            return new ResponseEntity<>(upcomingCourses, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/club/{clubId}/courses")
    public ResponseEntity<List<Course>> getCoursesByClub(@PathVariable Integer clubId) {
        try {
            List<Course> courses = courseService.getCoursesByClub(clubId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/coursetype/{courseTypeId}/courses")
    public ResponseEntity<List<Course>> getCoursesByCourseType(@PathVariable Long courseTypeId) {
        try {
            List<Course> courses = courseService.getCoursesByCourseType(courseTypeId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/agerange/{ageRangeId}/courses")
    public ResponseEntity<List<Course>> getCoursesByAgeRange(@PathVariable Long ageRangeId) {
        try {
            List<Course> courses = courseService.getCoursesByAgeRange(ageRangeId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/courses/upcoming")
    public List<Course> getUpcomingCourses() {
        return courseService.getUpcomingCourses();
    }

    @GetMapping("/courses/between")
    public List<Course> getCoursesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        return courseService.getCoursesBetweenDates(start, end);
    }

    @PostMapping("/course")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        try {
            Course updatedCourse = courseService.updateCourse(id, course);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
