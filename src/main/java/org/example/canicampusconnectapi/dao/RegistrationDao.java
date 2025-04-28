package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistrationDao extends JpaRepository<Registration, Long> {
    // Find registrations by dog
    List<Registration> findByDog(Dog dog);
    List<Registration> findByDogId(Long dogId);

    // Find registrations by course
    List<Registration> findByCourse(Course course);
    List<Registration> findByCourseId(Long courseId);

    // Find registrations by course type
    List<Registration> findByCourseCourseType(CourseType courseType);
    List<Registration> findByCourseCourseTypeId(Long courseTypeId);

    // Find registrations by status
    List<Registration> findByStatus(RegistrationStatus status);

    // Find registrations by date
    List<Registration> findByRegistrationDate(LocalDateTime date);
    List<Registration> findByRegistrationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find upcoming registrations (where course start date is in the future)
    @Query("SELECT r FROM Registration r WHERE r.course.startDatetime > CURRENT_TIMESTAMP")
    List<Registration> findUpcomingRegistrations();

    // Find upcoming registrations for a specific dog
    @Query("SELECT r FROM Registration r WHERE r.dog.id = ?1 AND r.course.startDatetime > CURRENT_TIMESTAMP")
    List<Registration> findUpcomingRegistrationsByDogId(Long dogId);

    // Find past registrations (where course end date is in the past)
    @Query("SELECT r FROM Registration r WHERE r.course.endDatetime < CURRENT_TIMESTAMP")
    List<Registration> findPastRegistrations();

    // Find past registrations for a specific dog
    @Query("SELECT r FROM Registration r WHERE r.dog.id = ?1 AND r.course.endDatetime < CURRENT_TIMESTAMP")
    List<Registration> findPastRegistrationsByDogId(Long dogId);

    // Find current registrations (where course is ongoing)
    @Query("SELECT r FROM Registration r WHERE r.course.startDatetime <= CURRENT_TIMESTAMP AND r.course.endDatetime >= CURRENT_TIMESTAMP")
    List<Registration> findCurrentRegistrations();

    // Find current registrations for a specific dog
    @Query("SELECT r FROM Registration r WHERE r.dog.id = ?1 AND r.course.startDatetime <= CURRENT_TIMESTAMP AND r.course.endDatetime >= CURRENT_TIMESTAMP")
    List<Registration> findCurrentRegistrationsByDogId(Long dogId);

    // Count registrations for a course
    Long countByCourseId(Long courseId);

    // Find registrations by course and status
    List<Registration> findByCourseAndStatus(Course course, RegistrationStatus status);
    List<Registration> findByCourseIdAndStatus(Long courseId, RegistrationStatus status);

    // Find registrations by dog and course
    List<Registration> findByDogIdAndCourseId(Long dogId, Long courseId);
}
