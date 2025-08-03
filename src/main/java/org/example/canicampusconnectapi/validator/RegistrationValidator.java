package org.example.canicampusconnectapi.validator;

import org.example.canicampusconnectapi.common.exception.BusinessException;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.dao.CourseDao;
import org.example.canicampusconnectapi.dao.RegistrationDao;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidator {

    private final RegistrationDao registrationDao;
    private final CourseDao courseDao;

    public RegistrationValidator(RegistrationDao registrationDao, CourseDao courseDao) {
        this.registrationDao = registrationDao;
        this.courseDao = courseDao;
    }
    /**
     * Valide qu'une inscription est possible pour un chien et un cours donnés.
     */
    public void validateRegistrationCreation(Registration registration) {
        validateRegistrationData(registration);
        validateNoDuplicateRegistration(registration);
        validateCourseExists(registration.getCourse().getId());
        validateCourseCapacity(registration.getCourse().getId());
    }

    private void validateRegistrationData(Registration registration) {
        if (registration.getCourse() == null || registration.getDog() == null) {
            throw new IllegalArgumentException("Le cours et le chien doivent être spécifiés");
        }
    }

    private void validateNoDuplicateRegistration(Registration registration) {
        if (registrationDao.existsByCourseIdAndDogId(
                registration.getCourse().getId(),
                registration.getDog().getId())) {
            throw new BusinessException("Ce chien est déjà inscrit à ce cours");
        }
    }

    private void validateCourseExists(Long courseId) {
        if (!courseDao.existsById(courseId)) {
            throw new ResourceNotFoundException("Cours introuvable");
        }
    }

    public void validateCourseCapacity(Long courseId) {
        Course course = courseDao.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable"));

        long confirmedRegistrations = registrationDao
                .countByCourseIdAndStatus(courseId, RegistrationStatus.CONFIRMED);

        if (confirmedRegistrations >= course.getMaxCapacity()) {
            throw new BusinessException("Le cours est complet, impossible de s'inscrire");
        }
    }
}