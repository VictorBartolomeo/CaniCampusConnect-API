package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.users.Club;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface CourseDao extends JpaRepository<Course, Long> {
    List<Course> findByCoachIdAndStartDatetimeAfter(Long coachId, Instant dateTime);
    List<Course> findByCoach(Coach coach);
    List<Course> findByClub(Club club);
    List<Course> findByCourseType(CourseType courseType);
    List<Course> findByStartDatetimeAfter(Instant dateTime);
    List<Course> findByStartDatetimeBetween(Instant startDateTime, Instant endDateTime);
    List<Course> findByClubAndStartDatetimeAfter(Club club, Instant dateTime);
    List<Course> findByCoachAndStartDatetimeAfter(Coach coach, Instant dateTime);
    List<Course> findByClubAndStartDatetimeBetween(Club club, Instant startDateTime, Instant endDateTime);
    List<Course> findByClubAndCoach(Club club, Coach coach);
    List<Course> findByClubAndCourseType(Club club, CourseType courseType);
}
