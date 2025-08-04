package org.example.canicampusconnectapi.service.rgpd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Service dédié à la gestion de l'audit RGPD
 */
@Service
public class RgpdAuditService {

    private static final Logger logger = LoggerFactory.getLogger(RgpdAuditService.class);

    /**
     * Définit tous les champs d'audit RGPD
     */
    public void setGdprAuditFields(Object entity, Long entityId) {
        logger.info("Mise à jour des champs d'audit RGPD pour l'entité ID {}", entityId);

        setAnonymizedFlag(entity, true);
        setAnonymizedAt(entity, LocalDateTime.now());
        setAnonymizedBy(entity, getCurrentUser());

        logger.info("Champs d'audit RGPD mis à jour pour l'entité ID {}", entityId);
    }

    /**
     * Marque l'entité comme anonymisée
     */
    private void setAnonymizedFlag(Object entity, boolean isAnonymized) {
        setFieldValue(entity, "isAnonymized", isAnonymized);
    }

    /**
     * Définit la date d'anonymisation
     */
    private void setAnonymizedAt(Object entity, LocalDateTime anonymizedAt) {
        setFieldValue(entity, "anonymizedAt", anonymizedAt);
    }

    /**
     * Définit qui a effectué l'anonymisation
     */
    private void setAnonymizedBy(Object entity, String anonymizedBy) {
        setFieldValue(entity, "anonymizedBy", anonymizedBy);
    }

    /**
     * Marque l'entité comme inactive si le champ existe
     */
    public void setInactiveIfExists(Object entity) {
        try {
            Field isActiveField = findFieldInHierarchy(entity.getClass(), "isActive");
            if (isActiveField != null) {
                isActiveField.setAccessible(true);
                isActiveField.set(entity, false);
                logger.debug("Entité marquée comme inactive");
            }
        } catch (IllegalAccessException e) {
            logger.warn("Impossible de marquer l'entité comme inactive", e);
        }
    }

    /**
     * Utilitaire pour définir la valeur d'un champ
     */
    private void setFieldValue(Object entity, String fieldName, Object value) {
        try {
            Field field = findFieldInHierarchy(entity.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(entity, value);
                logger.debug("Champ '{}' défini avec la valeur: {}", fieldName, value);
            } else {
                logger.debug("Champ '{}' non trouvé dans la hiérarchie de classes", fieldName);
            }
        } catch (IllegalAccessException e) {
            logger.error("Erreur lors de la définition du champ '{}' avec la valeur: {}", fieldName, value, e);
            throw new RuntimeException("Erreur lors de la mise à jour du champ d'audit: " + fieldName, e);
        }
    }

    /**
     * Recherche un champ dans la hiérarchie de classes
     */
    private Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * Récupère l'utilisateur actuellement connecté
     */
    private String getCurrentUser() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            logger.warn("Impossible de récupérer l'utilisateur actuel, utilisation de 'SYSTEM'", e);
            return "SYSTEM";
        }
    }
}