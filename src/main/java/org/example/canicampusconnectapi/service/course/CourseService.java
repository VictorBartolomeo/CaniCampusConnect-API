package org.example.canicampusconnectapi.service.course;

import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.AgeRange;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing courses.
 */
public interface CourseService {

    /**
     * Finds a course by its ID.
     *
     * @param id The ID of the course.
     * @return An Optional containing the course if found, otherwise empty.
     */
    Optional<Course> getCourseById(Long id);

    /**
     * Retrieves all courses for the default club.
     *
     * @return A list of all courses for the default club.
     */
    List<Course> getAllCourses();

    /**
     * Retrieves all courses for a specific coach in the default club.
     *
     * @param coachId The ID of the coach.
     * @return A list of courses for the coach.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the coach is not found.
     */
    List<Course> getCoursesByCoach(Long coachId);

    /**
     * Retrieves all courses for the default club.
     *
     * @param clubId The ID of the club (usually 1 for default club).
     * @return A list of courses for the club.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the club is not found.
     */
    List<Course> getCoursesByClub(Integer clubId);

    /**
     * Retrieves all courses of a specific course type in the default club.
     *
     * @param courseTypeId The ID of the course type.
     * @return A list of courses of the specified type.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the course type is not found.
     */
    List<Course> getCoursesByCourseType(Long courseTypeId);

    /**
     * Retrieves all upcoming courses for the default club.
     *
     * @return A list of upcoming courses.
     */
    List<Course> getUpcomingCourses();

    /**
     * Retrieves all courses between two dates for the default club.
     *
     * @param start The start date.
     * @param end The end date.
     * @return A list of courses between the specified dates.
     */
    List<Course> getCoursesBetweenDates(LocalDateTime start, LocalDateTime end);

    /**
     * Retrieves all courses for a specific age range in the default club.
     *
     * @param ageRangeId The ID of the age range.
     * @return A list of courses for the specified age range.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the age range is not found.
     */
    List<Course> getCoursesByAgeRange(Long ageRangeId);

    /**
     * Creates a new course. Validates coach, club, and course type existence.
     *
     * @param course The course object to create. Must have valid coach and course type IDs set.
     * @return The created course with its generated ID.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the specified coach, club, or course type does not exist.
     * @throws IllegalArgumentException if the coach or course type information is missing.
     */
    Course createCourse(Course course);

    /**
     * Deletes a course by its ID.
     *
     * @param id The ID of the course to delete.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the course with the given ID does not exist.
     */
    void deleteCourse(Long id);

    /**
     * Updates an existing course.
     *
     * @param id The ID of the course to update.
     * @param course The course data to update. Coach and course type can be updated if provided.
     * @return The updated course.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the course, coach, club, or course type does not exist.
     */
    Course updateCourse(Long id, Course course);
}