package org.example.canicampusconnectapi.factory;

import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RegistrationFactory {

    /**
     * Encapsulate object creation
     */
    public Registration prepareNewRegistration(Registration registration) {
        Registration newRegistration = new Registration();
        newRegistration.setCourse(registration.getCourse());
        newRegistration.setDog(registration.getDog());
        newRegistration.setStatus(RegistrationStatus.PENDING);
        return newRegistration;
    }
}