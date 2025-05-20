package org.example.canicampusconnectapi.service.registration;

import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;

import java.time.LocalDateTime;
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

    List<Registration> findByRegistrationDate(LocalDateTime date);

    List<Registration> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end);

    List<Registration> findUpcoming();

    List<Registration> findUpcomingByDogId(Long dogId);

    List<Registration> findPast();

    List<Registration> findPastByDogId(Long dogId);

    List<Registration> findCurrent();

    List<Registration> findCurrentByDogId(Long dogId);

    long countByCourseId(Long courseId);

    boolean deleteById(Long id);
}