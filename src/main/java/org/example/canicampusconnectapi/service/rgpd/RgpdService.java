package org.example.canicampusconnectapi.service.rgpd;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.security.annotation.rgpd.RgpdEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * Service principal d'orchestration pour les opérations RGPD
 */
@Service
@RequiredArgsConstructor
public class RgpdService {

    private static final Logger logger = LoggerFactory.getLogger(RgpdService.class);

    private final FieldAnonymizationService fieldAnonymizationService;
    private final RgpdAuditService auditService;
    private final EntityManager entityManager;

    /**
     * Anonymise une entité et ses relations selon les règles RGPD
     */
    @Transactional
    public <T> void anonymizeEntity(Class<T> entityClass, Long id) {
        logger.info("Début de l'anonymisation RGPD pour {} ID {}", entityClass.getSimpleName(), id);

        validateRgpdEntity(entityClass);
        T entity = findEntityOrThrow(entityClass, id);

        fieldAnonymizationService.anonymizeAllFields(entity, entityClass, id);

        auditService.setInactiveIfExists(entity);

        auditService.setGdprAuditFields(entity, id);

        anonymizeRelatedEntities(entity);

        entityManager.merge(entity);

        logger.info("Anonymisation RGPD terminée avec succès pour {} ID {}", entityClass.getSimpleName(), id);
    }

    /**
     * Vérifie si une entité est déjà anonymisée
     */
    public <T> boolean isAnonymized(Class<T> entityClass, Long id) {
        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            return false;
        }

        try {
            Field isAnonymizedField = findFieldInHierarchy(entityClass, "isAnonymized");
            if (isAnonymizedField != null) {
                isAnonymizedField.setAccessible(true);
                return (boolean) isAnonymizedField.get(entity);
            }
            return false;
        } catch (IllegalAccessException e) {
            logger.error("Erreur lors de la vérification du statut d'anonymisation pour {} ID {}",
                    entityClass.getSimpleName(), id, e);
            return false;
        }
    }

    /**
     * Valide que la classe est marquée comme entité RGPD
     */
    private void validateRgpdEntity(Class<?> clazz) {
        if (!hasRgpdEntityAnnotation(clazz)) {
            throw new IllegalArgumentException(
                    "L'entité " + clazz.getSimpleName() + " n'est pas marquée comme @RgpdEntity"
            );
        }
    }

    /**
     * Trouve une entité ou lève une exception
     */
    private <T> T findEntityOrThrow(Class<T> entityClass, Long id) {
        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new ResourceNotFoundException(
                    String.format("Entité %s non trouvée avec l'ID: %d", entityClass.getSimpleName(), id)
            );
        }
        return entity;
    }

    /**
     * Vérifie si une classe a l'annotation @RgpdEntity dans sa hiérarchie
     */
    private boolean hasRgpdEntityAnnotation(Class<?> clazz) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            if (currentClass.isAnnotationPresent(RgpdEntity.class)) {
                return true;
            }
            currentClass = currentClass.getSuperclass();
        }
        return false;
    }

    /**
     * Anonymise les entités liées selon les règles métier
     */
    private <T> void anonymizeRelatedEntities(T entity) {
        try {
            if (entity instanceof Owner owner) {
                anonymizeOwnerRelatedEntities(owner);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'anonymisation des entités liées", e);
        }
    }

    /**
     * Anonymise les entités liées à un propriétaire (ses chiens)
     */
    private void anonymizeOwnerRelatedEntities(Owner owner) {
        if (owner.getDogs() != null) {
            for (Dog dog : owner.getDogs()) {
                if (!isAnonymized(Dog.class, dog.getId())) {
                    logger.info("Anonymisation du chien ID {} lié au propriétaire ID {}",
                            dog.getId(), owner.getId());
                    anonymizeEntity(Dog.class, dog.getId());
                }
            }
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
}