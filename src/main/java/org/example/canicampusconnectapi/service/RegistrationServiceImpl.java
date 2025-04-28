package org.example.canicampusconnectapi.service;

import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.RegistrationDao;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Important pour les opérations d'écriture

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service // Indique que c'est un bean de service Spring
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationDao registrationDao;
    private final DogDao dogDao; // Injecté si nécessaire pour valider/récupérer Dog
    private final CourseDao courseDao; // Injecté si nécessaire pour valider/récupérer Course

    @Autowired
    public RegistrationServiceImpl(RegistrationDao registrationDao, DogDao dogDao, CourseDao courseDao) {
        this.registrationDao = registrationDao;
        this.dogDao = dogDao;
        this.courseDao = courseDao;
    }

    @Override
    @Transactional(readOnly = true) // Bon pour les opérations de lecture
    public Optional<Registration> findById(Long id) {
        return registrationDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findAll() {
        return registrationDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findByDogId(Long dogId) {
        // Pourrait ajouter une vérification si le chien existe
        // dogDao.findById(dogId).orElseThrow(() -> new RuntimeException("Dog not found"));
        return registrationDao.findByDogId(dogId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findByCourseId(Long courseId) {
        // Pourrait ajouter une vérification si le cours existe
        // courseDao.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        return registrationDao.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findByCourseTypeId(Long courseTypeId) {
        // Pas besoin de vérifier le courseType ici car le DAO le fait via la jointure
        return registrationDao.findByCourseCourseTypeId(courseTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findByStatus(RegistrationStatus status) {
        return registrationDao.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findByRegistrationDate(LocalDateTime date) {
        return registrationDao.findByRegistrationDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findByRegistrationDateBetween(LocalDateTime start, LocalDateTime end) {
        return registrationDao.findByRegistrationDateBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findUpcoming() {
        return registrationDao.findUpcomingRegistrations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findUpcomingByDogId(Long dogId) {
        // dogDao.findById(dogId).orElseThrow(() -> new RuntimeException("Dog not found"));
        return registrationDao.findUpcomingRegistrationsByDogId(dogId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findPast() {
        return registrationDao.findPastRegistrations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findPastByDogId(Long dogId) {
        // dogDao.findById(dogId).orElseThrow(() -> new RuntimeException("Dog not found"));
        return registrationDao.findPastRegistrationsByDogId(dogId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findCurrent() {
        return registrationDao.findCurrentRegistrations();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Registration> findCurrentByDogId(Long dogId) {
        // dogDao.findById(dogId).orElseThrow(() -> new RuntimeException("Dog not found"));
        return registrationDao.findCurrentRegistrationsByDogId(dogId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCourseId(Long courseId) {
        // courseDao.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        return registrationDao.countByCourseId(courseId);
    }

    @Override
    @Transactional // Transactionnel car c'est une opération d'écriture
    public Registration create(Registration registration) {
        // Validation de base (on pourrait ajouter plus de logique ici)
        if (registration.getDog() == null || registration.getDog().getId() == null) {
            throw new IllegalArgumentException("Dog ID cannot be null for registration");
        }
        if (registration.getCourse() == null || registration.getCourse().getId() == null) {
            throw new IllegalArgumentException("Course ID cannot be null for registration");
        }

        // Vérifier si le chien existe
        Dog dog = dogDao.findById(registration.getDog().getId())
                .orElseThrow(() -> new RuntimeException("Dog not found with id: " + registration.getDog().getId()));
        // Vérifier si le cours existe
        Course course = courseDao.findById(registration.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + registration.getCourse().getId()));

        // Logique métier : Vérifier si le cours est complet
        long currentRegistrations = registrationDao.countByCourseId(course.getId());
        if (currentRegistrations >= course.getMaxCapacity()) {
            throw new RuntimeException("Course is full. Cannot register.");
        }

        // Logique métier : Vérifier si ce chien est déjà inscrit à ce cours
        List<Registration> existingRegistrations = registrationDao.findByDogIdAndCourseId(dog.getId(), course.getId());
        if (!existingRegistrations.isEmpty()) {
            throw new RuntimeException("Dog is already registered for this course.");
        }

        // Définir les valeurs par défaut avant de sauvegarder
        registration.setRegistrationDate(LocalDateTime.now());
        if (registration.getStatus() == null) {
            registration.setStatus(RegistrationStatus.PENDING); // Ou un autre statut par défaut
        }
        // Assigner les entités complètes récupérées
        registration.setDog(dog);
        registration.setCourse(course);

        return registrationDao.save(registration);
    }

    @Override
    @Transactional // Transactionnel car c'est une opération d'écriture
    public Optional<Registration> update(Long id, Registration registrationDetails) {
        return registrationDao.findById(id)
                .map(existingRegistration -> {
                    // Mettre à jour uniquement les champs autorisés (ici, juste le statut comme exemple)
                    if (registrationDetails.getStatus() != null) {
                        existingRegistration.setStatus(registrationDetails.getStatus());
                    }
                    // On ne met PAS à jour dog, course, ou registrationDate lors d'un update typique
                    // Si d'autres champs doivent être modifiables, ajoutez-les ici.
                    return registrationDao.save(existingRegistration);
                });
    }

    @Override
    @Transactional // Transactionnel car c'est une opération d'écriture
    public boolean deleteById(Long id) {
        if (registrationDao.existsById(id)) {
            registrationDao.deleteById(id);
            return true;
        }
        return false;
    }
}