package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.CourseType;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RegistrationDao extends JpaRepository<Registration, Long> {
    List<Registration> findByDogId(Long dogId);

    List<Registration> findByCourseId(Long courseId);

    List<Registration> findByStatus(RegistrationStatus status);

    List<Registration> findByRegistrationDate(Instant date);

    List<Registration> findByRegistrationDateBetween(Instant start, Instant end);

    List<Registration> findByCourseStartDatetimeAfter(Instant date);

    List<Registration> findByDogIdAndCourseStartDatetimeAfter(Long dogId, Instant date);

    List<Registration> findByCourseEndDatetimeBefore(Instant date);

    List<Registration> findByDogIdAndCourseEndDatetimeBefore(Long dogId, Instant date);

    List<Registration> findByCourseStartDatetimeBeforeAndCourseEndDatetimeAfter(
            Instant startBefore, Instant endAfter);

    List<Registration> findByDogIdAndCourseStartDatetimeBeforeAndCourseEndDatetimeAfter(
            Long dogId, Instant startBefore, Instant endAfter);

    long countByCourseId(Long courseId);

    long countByCourseIdAndStatus(Long courseId, RegistrationStatus status);

    boolean existsByCourseIdAndDogId(Long courseId, Long dogId);

    List<Registration> findByCourseCoachIdAndStatus(Long coachId, RegistrationStatus status);
    /**
     * Trouve les registrations par coach, statut et date de début de cours après une date donnée
     */
    List<Registration> findByCourseCoachIdAndStatusAndCourseStartDatetimeAfter(
            Long coachId, RegistrationStatus status, Instant dateTime);

    /**
     * Trouve les registrations par statut et date de début de cours avant une date donnée
     */
    List<Registration> findByStatusAndCourseStartDatetimeBefore(
            RegistrationStatus status, Instant dateTime);
    List<Registration> findByDogIdAndCourseId(Long dogId, Long courseId);

}
