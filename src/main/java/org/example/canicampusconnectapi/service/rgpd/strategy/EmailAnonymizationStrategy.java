package org.example.canicampusconnectapi.service.rgpd.strategy;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Stratégie d'anonymisation pour les emails
 * Génère un email unique avec timestamp pour éviter les conflits
 */
@Component
public class EmailAnonymizationStrategy implements AnonymizationStrategy {

    @Override
    public boolean canHandle(Field field) {
        return field.getName().equalsIgnoreCase("email");
    }

    @Override
    public Object anonymize(Field field, Object entity, Long entityId) {
        long timestamp = System.currentTimeMillis();
        return "deleted-" + timestamp + "@anonymized.com";
    }

    @Override
    public int getPriority() {
        return 1; // Haute priorité
    }
}