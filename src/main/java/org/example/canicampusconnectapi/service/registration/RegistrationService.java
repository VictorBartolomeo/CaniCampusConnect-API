package org.example.canicampusconnectapi.service.registration;

import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RegistrationService {

    Registration create(Registration registration);

    Optional<Registration> update(Long id, Registration registration);

    Optional<Registration> updateStatus(Long id, RegistrationStatus status);

    Optional<Registration> findById(Long id);

    List<Registration> findAll();

    List<Registration> findByDogId(Long dogId);

    List<Registration> findByCourseId(Long courseId);

    List<Registration> findByStatus(RegistrationStatus status);

    List<Registration> findByRegistrationDate(Instant date);

    List<Registration> findByRegistrationDateBetween(Instant start, Instant end);

    List<Registration> findUpcoming();

    List<Registration> findUpcomingByDogId(Long dogId);

    List<Registration> findPast();

    List<Registration> findPastByDogId(Long dogId);

    List<Registration> findCurrent();

    List<Registration> findCurrentByDogId(Long dogId);

    long countByCourseId(Long courseId);

    boolean deleteById(Long id);

    List<Registration> findPendingRegistrationsByCoachId(Long coachId);
    /**
     * Expire automatiquement les registrations en attente pour des cours passés
     * et retourne seulement les registrations pending pour des cours futurs
     */
    List<Registration> findActivePendingRegistrationsByCoachId(Long coachId);

    /**
     * Expire toutes les registrations PENDING pour des cours passés
     */
    void expirePastPendingRegistrations();

}