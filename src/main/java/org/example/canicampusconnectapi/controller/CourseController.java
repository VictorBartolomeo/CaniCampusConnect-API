package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.course.CourseService;
import org.example.canicampusconnectapi.service.dog.DogService;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@IsClubOwner
public class CourseController {

    private final CourseService courseService;
    protected DogService dogService;

    @Autowired
    public CourseController(CourseService courseService, DogService dogService) {
        this.courseService = courseService;
        this.dogService = dogService;
    }

    @IsOwner
    @JsonView({OwnerViewCourse.class})
    @GetMapping("/course/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @IsOwner
    @JsonView({OwnerViewCourse.class})
    @GetMapping("/dog/{dogId}/courses")
    public ResponseEntity<?> getCoursesByDog(@PathVariable Long dogId, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            // Vérifier l'authentification
            if (userDetails == null || userDetails.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Utilisateur non authentifié"));
            }

            // Vérifier si l'utilisateur est ClubOwner
            boolean isClubOwner = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLUB_OWNER"));

            if (!isClubOwner) {
                // Owner : vérifier que le chien lui appartient
                Optional<Dog> dogOptional = dogService.getDogByIdAndOwnerId(dogId, userDetails.getUserId());
                if (dogOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Accès refusé : ce chien ne vous appartient pas"));
                }
            }

            // Récupérer tous les cours du chien via les registrations
            List<Course> courses = courseService.getCoursesByDogId(dogId);
            return ResponseEntity.ok(courses);

        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Chien avec l'ID " + dogId + " introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur"));
        }
    }

    @IsOwner
    @JsonView(OwnerViewCourse.class)
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

    @IsCoach
    @JsonView({CoachView.class})
    @GetMapping("/coach/{coachId}/upcoming-courses")
    public ResponseEntity<List<Course>> getUpcomingCoursesByCoach(@PathVariable Long coachId) {
        try {
            List<Course> upcomingCourses = courseService.getUpcomingCoursesByCoach(coachId);
            return new ResponseEntity<>(upcomingCourses, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @JsonView(OwnerViewCourse.class)
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

    @JsonView(OwnerViewCourse.class)
    @PostMapping("/course")
    public ResponseEntity<Course> createCourse(@RequestBody @Validated(Course.CreateCourse.class) Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Supprime aussi les registrations qui sont liées
    @DeleteMapping("/course/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return new ResponseEntity<>(
                    Map.of("message", "Course and all associated registrations deleted successfully"),
                    HttpStatus.NO_CONTENT
            );
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(
                    Map.of("error", "Course not found with id: " + id),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("error", "Unexpected error occurred: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
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
