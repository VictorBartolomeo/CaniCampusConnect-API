package org.example.canicampusconnectapi.service.rgpd.strategy;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Stratégie d'anonymisation pour les numéros de puce des chiens
 */
@Component
public class ChipNumberAnonymizationStrategy implements AnonymizationStrategy {

    @Override
    public boolean canHandle(Field field) {
        return field.getName().equalsIgnoreCase("chipNumber");
    }

    @Override
    public Object anonymize(Field field, Object entity, Long entityId) {
        return "DLT-CHIP-" + String.format("%06d", entityId);
    }

    @Override
    public int getPriority() {
        return 2;
    }
}