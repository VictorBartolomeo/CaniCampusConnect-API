package org.example.canicampusconnectapi.service.courseDto;

import org.example.canicampusconnectapi.model.courseRelated.Course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing courseDtos.
 */
public interface CourseDtoService {

    /**
     * Finds a courseDto by its ID.
     *
     * @param id The ID of the courseDto.
     * @return An Optional containing the courseDto if found, otherwise empty.
     */
    Optional<CourseDto> getCourseDtoById(Long id);

    /**
     * Retrieves all courseDtos for the default club.
     *
     * @return A list of all courseDtos for the default club.
     */
    List<CourseDto> getAllCourseDtos();

    /**
     * Retrieves all courseDtos for a specific coach in the default club.
     *
     * @param coachId The ID of the coach.
     * @return A list of courseDtos for the coach.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the coach is not found.
     */
    List<CourseDto> getCourseDtosByCoach(Long coachId);

    /**
     * Retrieves all courseDtos for the default club.
     *
     * @param clubId The ID of the club (usually 1 for default club).
     * @return A list of courseDtos for the club.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the club is not found.
     */
    List<CourseDto> getCourseDtosByClub(Integer clubId);

    /**
     * Retrieves all courseDtos of a specific courseDto type in the default club.
     *
     * @param courseDtoTypeId The ID of the courseDto type.
     * @return A list of courseDtos of the specified type.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the courseDto type is not found.
     */
    List<CourseDto> getCourseDtosByCourseDtoType(Long courseDtoTypeId);

    /**
     * Retrieves all upcoming courseDtos for the default club.
     *
     * @return A list of upcoming courseDtos.
     */
    List<CourseDto> getUpcomingCourseDtos();

    /**
     * Retrieves all courseDtos between two dates for the default club.
     *
     * @param start The start date.
     * @param end   The end date.
     * @return A list of courseDtos between the specified dates.
     */
    List<CourseDto> getCourseDtosBetweenDates(LocalDateTime start, LocalDateTime end);

    /**
     * Retrieves all courseDtos for a specific age range in the default club.
     *
     * @param ageRangeId The ID of the age range.
     * @return A list of courseDtos for the specified age range.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the age range is not found.
     */
    List<CourseDto> getCourseDtosByAgeRange(Long ageRangeId);

    /**
     * Creates a new courseDto. Validates coach, club, and courseDto type existence.
     *
     * @param courseDto The courseDto object to create. Must have valid coach and courseDto type IDs set.
     * @return The created courseDto with its generated ID.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the specified coach, club, or courseDto type does not exist.
     * @throws IllegalArgumentException                                           if the coach or courseDto type information is missing.
     */
    CourseDto createCourseDto(CourseDto courseDto);

    /**
     * Deletes a courseDto by its ID.
     *
     * @param id The ID of the courseDto to delete.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the courseDto with the given ID does not exist.
     */
    void deleteCourseDto(Long id);

    /**
     * Updates an existing courseDto.
     *
     * @param id        The ID of the courseDto to update.
     * @param courseDto The courseDto data to update. Coach and courseDto type can be updated if provided.
     * @return The updated courseDto.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the courseDto, coach, club, or courseDto type does not exist.
     */
    CourseDto updateCourseDto(Long id, CourseDto courseDto);
}