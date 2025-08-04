package org.example.canicampusconnectapi.service.rgpd.strategy;

import org.example.canicampusconnectapi.security.annotation.rgpd.PersonalData;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Stratégie d'anonymisation par défaut
 * Utilise la valeur définie dans l'annotation @PersonalData
 */
@Component
public class DefaultAnonymizationStrategy implements AnonymizationStrategy {

    @Override
    public boolean canHandle(Field field) {
        return field.isAnnotationPresent(PersonalData.class);
    }

    @Override
    public Object anonymize(Field field, Object entity, Long entityId) {
        PersonalData annotation = field.getAnnotation(PersonalData.class);
        return annotation.anonymizeWith();
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE; // Priorité la plus basse
    }
}