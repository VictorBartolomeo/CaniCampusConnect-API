
package org.example.canicampusconnectapi.service.rgpd;

import org.example.canicampusconnectapi.factory.AnonymizationStrategyFactory;
import org.example.canicampusconnectapi.security.annotation.rgpd.PersonalData;
import org.example.canicampusconnectapi.service.rgpd.strategy.AnonymizationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * Service dédié à l'anonymisation des champs marqués @PersonalData
 */
@Service
public class FieldAnonymizationService {

    private static final Logger logger = LoggerFactory.getLogger(FieldAnonymizationService.class);

    private final AnonymizationStrategyFactory strategyFactory;

    public FieldAnonymizationService(AnonymizationStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    /**
     * Anonymise tous les champs marqués @PersonalData dans la hiérarchie de classes
     */
    public void anonymizeAllFields(Object entity, Class<?> entityClass, Long entityId) {
        logger.info("Début de l'anonymisation des champs pour l'entité {} avec ID {}",
                entityClass.getSimpleName(), entityId);

        Class<?> currentClass = entityClass;
        int anonymizedFields = 0;

        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(PersonalData.class)) {
                    anonymizeField(entity, field, entityId);
                    anonymizedFields++;
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        logger.info("Anonymisation terminée : {} champs anonymisés pour l'entité {} ID {}",
                anonymizedFields, entityClass.getSimpleName(), entityId);
    }

    /**
     * Anonymise un champ spécifique
     */
    private void anonymizeField(Object entity, Field field, Long entityId) {
        try {
            field.setAccessible(true);
            Object originalValue = field.get(entity);

            AnonymizationStrategy strategy = strategyFactory.getStrategy(field);
            Object anonymizedValue = strategy.anonymize(field, entity, entityId);

            field.set(entity, anonymizedValue);

            logger.debug("Champ '{}' anonymisé avec la stratégie {} (ID: {})",
                    field.getName(), strategy.getClass().getSimpleName(), entityId);

        } catch (IllegalAccessException e) {
            String errorMsg = String.format("Impossible d'anonymiser le champ '%s' pour l'entité ID %d",
                    field.getName(), entityId);
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
}