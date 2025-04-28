package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.CourseTypeDao;
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.RegistrationDao;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
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
public class RegistrationController {

    protected RegistrationDao registrationDao;
    protected DogDao dogDao;
    protected CourseDao courseDao;
    protected CourseTypeDao courseTypeDao;

    @Autowired
    public RegistrationController(RegistrationDao registrationDao, DogDao dogDao, CourseDao courseDao, CourseTypeDao courseTypeDao) {
        this.registrationDao = registrationDao;
        this.dogDao = dogDao;
        this.courseDao = courseDao;
        this.courseTypeDao = courseTypeDao;
    }

    // Get a registration by ID
    @GetMapping("/registration/{id}")
    public ResponseEntity<Registration> getRegistration(@PathVariable Long id) {
        Optional<Registration> optionalRegistration = registrationDao.findById(id);
        if (optionalRegistration.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalRegistration.get(), HttpStatus.OK);
    }

    // Get all registrations
    @GetMapping("/registrations")
    public List<Registration> getAllRegistrations() {
        return registrationDao.findAll();
    }

    // Get registrations by dog
    @GetMapping("/dog/{dogId}/registrations")
    public ResponseEntity<List<Registration>> getRegistrationsByDog(@PathVariable Long dogId) {
        Optional<Dog> optionalDog = dogDao.findById(dogId);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Registration> registrations = registrationDao.findByDog(optionalDog.get());
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get registrations by course
    @GetMapping("/course/{courseId}/registrations")
    public ResponseEntity<List<Registration>> getRegistrationsByCourse(@PathVariable Long courseId) {
        Optional<Course> optionalCourse = courseDao.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Registration> registrations = registrationDao.findByCourse(optionalCourse.get());
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get registrations by course type
    @GetMapping("/coursetype/{courseTypeId}/registrations")
    public ResponseEntity<List<Registration>> getRegistrationsByCourseType(@PathVariable Long courseTypeId) {
        Optional<CourseType> optionalCourseType = courseTypeDao.findById(courseTypeId);
        if (optionalCourseType.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Registration> registrations = registrationDao.findByCourseCourseType(optionalCourseType.get());
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get registrations by status
    @GetMapping("/registrations/status/{status}")
    public List<Registration> getRegistrationsByStatus(@PathVariable RegistrationStatus status) {
        return registrationDao.findByStatus(status);
    }

    // Get registrations by date
    @GetMapping("/registrations/date")
    public List<Registration> getRegistrationsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return registrationDao.findByRegistrationDate(date);
    }

    // Get registrations by date range
    @GetMapping("/registrations/between")
    public List<Registration> getRegistrationsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return registrationDao.findByRegistrationDateBetween(start, end);
    }

    // Get upcoming registrations
    @GetMapping("/registrations/upcoming")
    public List<Registration> getUpcomingRegistrations() {
        return registrationDao.findUpcomingRegistrations();
    }

    // Get upcoming registrations for a specific dog
    @GetMapping("/dog/{dogId}/registrations/upcoming")
    public ResponseEntity<List<Registration>> getUpcomingRegistrationsByDog(@PathVariable Long dogId) {
        if (!dogDao.existsById(dogId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Registration> registrations = registrationDao.findUpcomingRegistrationsByDogId(dogId);
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get past registrations
    @GetMapping("/registrations/past")
    public List<Registration> getPastRegistrations() {
        return registrationDao.findPastRegistrations();
    }

    // Get past registrations for a specific dog
    @GetMapping("/dog/{dogId}/registrations/past")
    public ResponseEntity<List<Registration>> getPastRegistrationsByDog(@PathVariable Long dogId) {
        if (!dogDao.existsById(dogId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Registration> registrations = registrationDao.findPastRegistrationsByDogId(dogId);
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Get current registrations
    @GetMapping("/registrations/current")
    public List<Registration> getCurrentRegistrations() {
        return registrationDao.findCurrentRegistrations();
    }

    // Get current registrations for a specific dog
    @GetMapping("/dog/{dogId}/registrations/current")
    public ResponseEntity<List<Registration>> getCurrentRegistrationsByDog(@PathVariable Long dogId) {
        if (!dogDao.existsById(dogId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Registration> registrations = registrationDao.findCurrentRegistrationsByDogId(dogId);
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }

    // Count registrations for a course
    @GetMapping("/course/{courseId}/registrations/count")
    public ResponseEntity<Long> countRegistrationsByCourse(@PathVariable Long courseId) {
        if (!courseDao.existsById(courseId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Long count = registrationDao.countByCourseId(courseId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Create a new registration
    @PostMapping("/registration")
    public ResponseEntity<Registration> createRegistration(@RequestBody Registration registration) {
        // Verify that the dog exists
        if (registration.getDog() == null || registration.getDog().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Dog> optionalDog = dogDao.findById(registration.getDog().getId());
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Verify that the course exists
        if (registration.getCourse() == null || registration.getCourse().getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Course> optionalCourse = courseDao.findById(registration.getCourse().getId());
        if (optionalCourse.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Check if the course is full
        Course course = optionalCourse.get();
        Long currentRegistrations = registrationDao.countByCourseId(course.getId());
        if (currentRegistrations >= course.getMaxCapacity()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Check if the dog is already registered for this course
        List<Registration> existingRegistrations = registrationDao.findByDogIdAndCourseId(registration.getDog().getId(), course.getId());
        if (!existingRegistrations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Set the entities with the full objects from the database
        registration.setDog(optionalDog.get());
        registration.setCourse(course);

        // Set registration date to now if not provided
        if (registration.getRegistrationDate() == null) {
            registration.setRegistrationDate(LocalDateTime.now());
        }

        // Always set status to PENDING for new registrations
        // This ensures that all registrations require validation by a club owner or coach
        registration.setStatus(RegistrationStatus.PENDING);

        registrationDao.save(registration);
        return new ResponseEntity<>(registration, HttpStatus.CREATED);
    }

    // Update an existing registration
    @PutMapping("/registration/{id}")
    public ResponseEntity<Void> updateRegistration(@PathVariable Long id, @RequestBody Registration registration) {
        Optional<Registration> optionalRegistration = registrationDao.findById(id);
        if (optionalRegistration.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Verify that the dog exists if it's being updated
        if (registration.getDog() != null && registration.getDog().getId() != null) {
            Optional<Dog> optionalDog = dogDao.findById(registration.getDog().getId());
            if (optionalDog.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            registration.setDog(optionalDog.get());
        } else {
            // Keep the existing dog if not provided
            registration.setDog(optionalRegistration.get().getDog());
        }

        // Verify that the course exists if it's being updated
        if (registration.getCourse() != null && registration.getCourse().getId() != null) {
            Optional<Course> optionalCourse = courseDao.findById(registration.getCourse().getId());
            if (optionalCourse.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            registration.setCourse(optionalCourse.get());
        } else {
            // Keep the existing course if not provided
            registration.setCourse(optionalRegistration.get().getCourse());
        }

        // Keep the original registration date
        registration.setRegistrationDate(optionalRegistration.get().getRegistrationDate());

        registration.setId(id);
        registrationDao.save(registration);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete a registration
    @DeleteMapping("/registration/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        Optional<Registration> optionalRegistration = registrationDao.findById(id);
        if (optionalRegistration.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        registrationDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
