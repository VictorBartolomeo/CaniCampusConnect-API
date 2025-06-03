
package org.example.canicampusconnectapi.service;

import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.RegistrationDao;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.service.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationDao registrationDao;
    private final CourseDao courseDao;

    @Autowired
    public RegistrationServiceImpl(RegistrationDao registrationDao, CourseDao courseDao) {
        this.registrationDao = registrationDao;
        this.courseDao = courseDao;
    }

    /**
     * Crée une nouvelle inscription pour un chien à un cours.
     * Vérifie si le cours n'est pas plein avant l'inscription.
     * Met le statut de l'inscription en PENDING par défaut.
     */
    @Override
    @Transactional
    public Registration create(Registration registration) {
        List<Registration> existingRegistrations = registrationDao.findByDogIdAndCourseId(
                registration.getDog().getId(),
                registration.getCourse().getId()
        );

        if (!existingRegistrations.isEmpty()) {
            throw new IllegalArgumentException("Ce chien est déjà inscrit à ce cours");
        }

        // Vérifier si l'objet registration est valide
        if (registration.getCourse() == null || registration.getDog() == null) {
            throw new IllegalArgumentException("Le cours et le chien doivent être spécifiés");
        }

        // Récupérer le cours complet pour vérifier la capacité
        Course course = courseDao.findById(registration.getCourse().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cours introuvable"));

        // Vérifier que la jauge du cours n'est pas pleine
        long confirmedRegistrations = registrationDao.countByCourseIdAndStatus(
                course.getId(), RegistrationStatus.CONFIRMED);

        if (confirmedRegistrations >= course.getMaxCapacity()) {
            throw new RuntimeException("Le cours est complet, impossible de s'inscrire");
        }

        // Vérifier que le chien n'est pas déjà inscrit à ce cours
        if (registrationDao.existsByCourseIdAndDogId(
                course.getId(), registration.getDog().getId())) {
            throw new RuntimeException("Ce chien est déjà inscrit à ce cours");
        }

        // Régler les valeurs par défaut
        registration.setStatus(RegistrationStatus.PENDING);
        registration.setRegistrationDate(LocalDateTime.now());

        // Sauvegarder l'inscription
        return registrationDao.save(registration);
    }

    /**
     * Met à jour une inscription existante.
     */
    @Override
    @Transactional
    public Optional<Registration> update(Long id, Registration details) {
        return registrationDao.findById(id).map(existingRegistration -> {
            // Si on met à jour vers CONFIRMED, vérifier que le cours n'est pas plein
            if (details.getStatus() == RegistrationStatus.CONFIRMED &&
                    existingRegistration.getStatus() != RegistrationStatus.CONFIRMED) {

                Course course = existingRegistration.getCourse();
                long confirmedRegistrations = registrationDao.countByCourseIdAndStatus(
                        course.getId(), RegistrationStatus.CONFIRMED);

                if (confirmedRegistrations >= course.getMaxCapacity()) {
                    throw new RuntimeException("Le cours est complet, impossible de confirmer cette inscription");
                }
            }

            // Ne mettre à jour que le statut pour le moment
            if (details.getStatus() != null) {
                existingRegistration.setStatus(details.getStatus());
            }

            return registrationDao.save(existingRegistration);
        });
    }

    /**
     * Met à jour uniquement le statut d'une inscription.
     * Méthode dédiée pour simplifier la mise à jour du statut.
     */
    @Override
    @Transactional
    public Optional<Registration> updateStatus(Long id, RegistrationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Le statut ne peut pas être null");
        }

        return registrationDao.findById(id).map(existingRegistration -> {
            // Si on passe à CONFIRMED, vérifier que le cours n'est pas plein
            if (status == RegistrationStatus.CONFIRMED &&
                    existingRegistration.getStatus() != RegistrationStatus.CONFIRMED) {

                Course course = existingRegistration.getCourse();
                long confirmedRegistrations = registrationDao.countByCourseIdAndStatus(
                        course.getId(), RegistrationStatus.CONFIRMED);

                if (confirmedRegistrations >= course.getMaxCapacity()) {
                    throw new RuntimeException("Le cours est complet, impossible de confirmer cette inscription");
                }
            }

            existingRegistration.setStatus(status);
            return registrationDao.save(existingRegistration);
        });
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
    public List<Registration> findByRegistrationDate(LocalDateTime date) {
        return registrationDao.findByRegistrationDate(date);
    }

    @Override
    public List<Registration> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end) {
        return registrationDao.findByRegistrationDateBetween(start, end);
    }

    @Override
    public List<Registration> findUpcoming() {
        return registrationDao.findByCourseStartDatetimeAfter(LocalDateTime.now());
    }

    @Override
    public List<Registration> findUpcomingByDogId(Long dogId) {
        return registrationDao.findByDogIdAndCourseStartDatetimeAfter(dogId, LocalDateTime.now());
    }

    @Override
    public List<Registration> findPast() {
        return registrationDao.findByCourseEndDatetimeBefore(LocalDateTime.now());
    }

    @Override
    public List<Registration> findPastByDogId(Long dogId) {
        return registrationDao.findByDogIdAndCourseEndDatetimeBefore(dogId, LocalDateTime.now());
    }

    @Override
    public List<Registration> findCurrent() {
        LocalDateTime now = LocalDateTime.now();
        return registrationDao.findByCourseStartDatetimeBeforeAndCourseEndDatetimeAfter(now, now);
    }

    @Override
    public List<Registration> findCurrentByDogId(Long dogId) {
        LocalDateTime now = LocalDateTime.now();
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
        LocalDateTime now = LocalDateTime.now();
        return registrationDao.findByCourseCoachIdAndStatusAndCourseStartDatetimeAfter(
                coachId, RegistrationStatus.PENDING, now);
    }

    /**
     * Expire automatiquement toutes les registrations PENDING pour des cours passés
     */
    @Override
    @Transactional
    public void expirePastPendingRegistrations() {
        LocalDateTime now = LocalDateTime.now();

        // Trouver toutes les registrations PENDING pour des cours passés
        List<Registration> expiredRegistrations = registrationDao
                .findByStatusAndCourseStartDatetimeBefore(RegistrationStatus.PENDING, now);

        // Les marquer comme REJECTED
        for (Registration registration : expiredRegistrations) {
            registration.setStatus(RegistrationStatus.REFUSED);
            registrationDao.save(registration);
        }
    }
}