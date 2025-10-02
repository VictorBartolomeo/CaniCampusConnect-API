package org.example.canicampusconnectapi.service.registration;

import lombok.RequiredArgsConstructor;
import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.RegistrationDao;
import org.example.canicampusconnectapi.factory.RegistrationFactory;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.validator.RegistrationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationDao registrationDao;
    private final CourseDao courseDao;
    private final RegistrationValidator registrationValidator;
    private final RegistrationFactory registrationFactory;

    /**
     * Crée une nouvelle inscription
     */
    @Override
    @Transactional
    public Registration create(Registration registration) {
        registrationValidator.validateRegistrationCreation(registration);
        Registration preparedRegistration = registrationFactory.prepareNewRegistration(registration);
        return registrationDao.save(preparedRegistration);
    }

    /**
     * Met à jour une inscription existante.
     */
    @Override
    @Transactional
    public Optional<Registration> update(Long id, Registration details) {
        return registrationDao.findById(id).map(existingRegistration -> {
            if (details.getStatus() == RegistrationStatus.CONFIRMED &&
                    existingRegistration.getStatus() != RegistrationStatus.CONFIRMED) {
                registrationValidator.validateCourseCapacity(existingRegistration.getCourse().getId());
            }

            // Ne mettre à jour que le statut pour le moment
            if (details.getStatus() != null) {
                existingRegistration.setStatus(details.getStatus());
            }

            return registrationDao.save(existingRegistration);
        });
    }

    @Override
    @Transactional
    public Optional<Registration> updateStatus(Long id, RegistrationStatus newStatus) {
        //Vérifie que le status n'est pas null (normalement impossible car mit directement dans le back a PENDING)
        validateStatusNotNull(newStatus);

        return registrationDao.findById(id) // Recherche en BDD par son id
                .map(existingRegistration ->
                        updateRegistrationStatus(existingRegistration, newStatus));
        //Vérifie l'ancien et le nouveau status, si différents et que le nouveau est CONFIRMED ou REFUSED, le met à jour
    }

    private void validateStatusNotNull(RegistrationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Le statut ne peut pas être null");
        }
    }

    private Registration updateRegistrationStatus(Registration registration, RegistrationStatus newStatus) {
        if (isConfirmationRequest(registration.getStatus(), newStatus)) {
            registrationValidator.validateCourseCapacity(registration.getCourse().getId());
        }
        //Si jamais c'est REFUSED, la vérification n'a pas lieu et ca set le newStatus directement

        registration.setStatus(newStatus);
        return registrationDao.save(registration);
    }

    private boolean isConfirmationRequest(RegistrationStatus currentStatus, RegistrationStatus newStatus) {
        return newStatus == RegistrationStatus.CONFIRMED &&
                currentStatus != RegistrationStatus.CONFIRMED;
    }


    @Override
    public Optional<Registration> findById(Long id) {
        return registrationDao.findById(id);
    }

    @Override
    public List<Registration> findAll() {
        return registrationDao.findAll();
    }

    @Override
    public List<Registration> findByDogId(Long dogId) {
        return registrationDao.findByDogId(dogId);
    }

    @Override
    public List<Registration> findByCourseId(Long courseId) {
        return registrationDao.findByCourseId(courseId);
    }

    @Override
    public List<Registration> findByStatus(RegistrationStatus status) {
        return registrationDao.findByStatus(status);
    }

    @Override
    public List<Registration> findByRegistrationDate(Instant date) {
        return registrationDao.findByRegistrationDate(date);
    }

    @Override
    public List<Registration> findByRegistrationDateBetween(Instant start, Instant end) {
        return registrationDao.findByRegistrationDateBetween(start, end);
    }

    @Override
    public List<Registration> findUpcoming() {
        return registrationDao.findByCourseStartDatetimeAfter(Instant.now());
    }

    @Override
    public List<Registration> findUpcomingByDogId(Long dogId) {
        return registrationDao.findByDogIdAndCourseStartDatetimeAfter(dogId, Instant.now());
    }

    @Override
    public List<Registration> findPast() {
        return registrationDao.findByCourseEndDatetimeBefore(Instant.now());
    }

    @Override
    public List<Registration> findPastByDogId(Long dogId) {
        return registrationDao.findByDogIdAndCourseEndDatetimeBefore(dogId, Instant.now());
    }

    @Override
    public List<Registration> findCurrent() {
        Instant now = Instant.now();
        return registrationDao.findByCourseStartDatetimeBeforeAndCourseEndDatetimeAfter(now, now);
    }

    @Override
    public List<Registration> findCurrentByDogId(Long dogId) {
        Instant now = Instant.now();
        return registrationDao.findByDogIdAndCourseStartDatetimeBeforeAndCourseEndDatetimeAfter(
                dogId, now, now);
    }

    @Override
    public long countByCourseId(Long courseId) {
        return registrationDao.countByCourseId(courseId);
    }

    @Override
    public boolean deleteById(Long id) {
        if (registrationDao.existsById(id)) {
            registrationDao.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Trouve toutes les registrations en attente pour les cours d'un coach spécifique.
     */
    @Override
    public List<Registration> findPendingRegistrationsByCoachId(Long coachId) {
        return registrationDao.findByCourseCoachIdAndStatus(coachId, RegistrationStatus.PENDING);
    }

    /**
     * Trouve les registrations en attente pour des cours futurs d'un coach.
     * Expire automatiquement celles pour des cours passés.
     */
    @Override
    @Transactional
    public List<Registration> findActivePendingRegistrationsByCoachId(Long coachId) {
        // D'abord, expirer les registrations des cours passés
        expirePastPendingRegistrations();

        // Ensuite, retourner seulement les PENDING pour des cours futurs
        Instant now = Instant.now();
        return registrationDao.findByCourseCoachIdAndStatusAndCourseStartDatetimeAfter(
                coachId, RegistrationStatus.PENDING, now);
    }

    /**
     * Expire automatiquement toutes les registrations PENDING pour des cours passés
     */
    @Override
    @Transactional
    public void expirePastPendingRegistrations() {
        Instant now = Instant.now();

        List<Registration> expiredRegistrations = registrationDao
                .findByStatusAndCourseStartDatetimeBefore(RegistrationStatus.PENDING, now);

        // Les marquer comme REFUSED
        expiredRegistrations.forEach(registration -> {
            registration.setStatus(RegistrationStatus.REFUSED);
            registrationDao.save(registration);
        });
    }

    @Override
    @Transactional
    public void deleteAllByCourseId(Long courseId) {
        List<Registration> registrations = findByCourseId(courseId);
        registrations.forEach(registration -> deleteById(registration.getId()));
    }
}