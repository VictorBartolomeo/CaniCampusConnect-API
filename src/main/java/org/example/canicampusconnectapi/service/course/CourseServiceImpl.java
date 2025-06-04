package org.example.canicampusconnectapi.service.course;

import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.dao.AgeRangeDao;
import org.example.canicampusconnectapi.dao.ClubDao;
import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.CourseTypeDao;
import org.example.canicampusconnectapi.model.users.Club;
import org.example.canicampusconnectapi.model.courseRelated.AgeRange;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.example.canicampusconnectapi.model.users.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;
    private final CoachDao coachDao;
    private final ClubDao clubDao;
    private final CourseTypeDao courseTypeDao;
    private final AgeRangeDao ageRangeDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao, CoachDao coachDao, ClubDao clubDao, 
                            CourseTypeDao courseTypeDao, AgeRangeDao ageRangeDao) {
        this.courseDao = courseDao;
        this.coachDao = coachDao;
        this.clubDao = clubDao;
        this.courseTypeDao = courseTypeDao;
        this.ageRangeDao = ageRangeDao;
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseDao.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        // Get the default club (ID 1)
        Club defaultClub = getDefaultClub();
        return courseDao.findByClub(defaultClub);
    }

    @Override
    public List<Course> getCoursesByCoach(Long coachId) {
        Coach coach = coachDao.findById(coachId)
                .orElseThrow(() -> new ResourceNotFound("Coach not found with id: " + coachId));
        
        Club defaultClub = getDefaultClub();
        return courseDao.findByClubAndCoach(defaultClub, coach);
    }

    @Override
    public List<Course> getCoursesByClub(Integer clubId) {
        // For now, we only use the default club (ID 1)
        Club defaultClub = getDefaultClub();
        return courseDao.findByClub(defaultClub);
    }

    @Override
    public List<Course> getCoursesByCourseType(Long courseTypeId) {
        CourseType courseType = courseTypeDao.findById(courseTypeId)
                .orElseThrow(() -> new ResourceNotFound("Course type not found with id: " + courseTypeId));
        
        Club defaultClub = getDefaultClub();
        return courseDao.findByClubAndCourseType(defaultClub, courseType);
    }

    @Override
    public List<Course> getUpcomingCourses() {
        Club defaultClub = getDefaultClub();
        return courseDao.findByClubAndStartDatetimeAfter(defaultClub, Instant.now());
    }

    @Override
    public List<Course> getCoursesBetweenDates(Instant start, Instant end) {
        Club defaultClub = getDefaultClub();
        return courseDao.findByClubAndStartDatetimeBetween(defaultClub, start, end);
    }

    @Override
    public List<Course> getCoursesByAgeRange(Long ageRangeId) {
        AgeRange ageRange = ageRangeDao.findById(ageRangeId)
                .orElseThrow(() -> new ResourceNotFound("Age range not found with id: " + ageRangeId));
        
        Club defaultClub = getDefaultClub();
        
        // Get all course types for this age range
        List<CourseType> courseTypes = courseTypeDao.findByAgeRange(ageRange);
        
        // Get all courses for these course types
        return courseTypes.stream()
                .flatMap(courseType -> courseDao.findByClubAndCourseType(defaultClub, courseType).stream())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Course createCourse(Course course) {
        if (course.getCoach() == null || course.getCoach().getId() == null) {
            throw new IllegalArgumentException("Coach ID must be provided to create a course.");
        }
        
        Coach coach = coachDao.findById(course.getCoach().getId())
                .orElseThrow(() -> new ResourceNotFound("Coach not found with id: " + course.getCoach().getId()));
        
        Club defaultClub = getDefaultClub();
        
        if (course.getCourseType() == null || course.getCourseType().getId() == null) {
            throw new IllegalArgumentException("Course type ID must be provided to create a course.");
        }
        
        CourseType courseType = courseTypeDao.findById(course.getCourseType().getId())
                .orElseThrow(() -> new ResourceNotFound("Course type not found with id: " + course.getCourseType().getId()));
        
        // Set the full objects before saving
        course.setCoach(coach);
        course.setClub(defaultClub);
        course.setCourseType(courseType);
        
        // Clear ID to ensure creation, not update
        course.setId(null);
        
        return courseDao.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseDao.existsById(id)) {
            throw new ResourceNotFound("Course not found with id: " + id);
        }
        courseDao.deleteById(id);
    }

    @Override
    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        Course existingCourse = courseDao.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Course not found with id: " + id));
        
        // Update basic fields
        existingCourse.setTitle(courseDetails.getTitle());
        existingCourse.setDescription(courseDetails.getDescription());
        existingCourse.setStartDatetime(courseDetails.getStartDatetime());
        existingCourse.setEndDatetime(courseDetails.getEndDatetime());
        existingCourse.setMaxCapacity(courseDetails.getMaxCapacity());
        
        // Handle coach update only if provided in the request
        if (courseDetails.getCoach() != null && courseDetails.getCoach().getId() != null) {
            Coach coach = coachDao.findById(courseDetails.getCoach().getId())
                    .orElseThrow(() -> new ResourceNotFound("Coach not found with id: " + courseDetails.getCoach().getId()));
            existingCourse.setCoach(coach);
        }
        
        // Always use the default club
        Club defaultClub = getDefaultClub();
        existingCourse.setClub(defaultClub);
        
        // Handle course type update only if provided in the request
        if (courseDetails.getCourseType() != null && courseDetails.getCourseType().getId() != null) {
            CourseType courseType = courseTypeDao.findById(courseDetails.getCourseType().getId())
                    .orElseThrow(() -> new ResourceNotFound("Course type not found with id: " + courseDetails.getCourseType().getId()));
            existingCourse.setCourseType(courseType);
        }
        
        return courseDao.save(existingCourse);
    }
    
    /**
     * Helper method to get the default club (ID 1).
     * 
     * @return The default club.
     * @throws ResourceNotFound if the default club does not exist.
     */
    private Club getDefaultClub() {
        return clubDao.findById(1)
                .orElseThrow(() -> new ResourceNotFound("Default club not found"));
    }

    @Override
    public List<Course> getUpcomingCoursesByCoach(Long coachId) {
        Coach coach = coachDao.findById(coachId)
                .orElseThrow(() -> new ResourceNotFound("Coach not found with ID: " + coachId));

        Instant now = Instant.now();
        return courseDao.findByCoachIdAndStartDatetimeAfter(coachId, now);
    }
}