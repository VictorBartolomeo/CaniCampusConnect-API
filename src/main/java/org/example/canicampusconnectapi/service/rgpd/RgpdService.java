package org.example.canicampusconnectapi.service.rgpd;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.security.annotation.rgpd.PersonalData;
import org.example.canicampusconnectapi.security.annotation.rgpd.RgpdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Service
public class RgpdService {

    private final EntityManager entityManager;

    @Autowired
    public RgpdService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public <T> void anonymizeEntity(Class<T> entityClass, Long id) {
        if (!hasRgpdEntityAnnotation(entityClass)) {
            throw new IllegalArgumentException("L'entité " + entityClass.getSimpleName() + " n'est pas marquée comme @RgpdEntity.");
        }

        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new ResourceNotFound("Entité non trouvée avec l'ID: " + id);
        }

        // ✅ Anonymiser l'entité principale
        anonymizeAllFields(entity, entityClass, id); // ⭐ Passer l'ID pour l'unicité
        setInactiveIfExists(entity);
        setGdprAuditFields(entity, id);

        // ✅ Anonymiser les entités associées
        anonymizeRelatedEntities(entity);

        entityManager.merge(entity);
    }

    private <T> void anonymizeRelatedEntities(T entity) {
        try {
            if (entity instanceof Owner) {
                Owner owner = (Owner) entity;
                if (owner.getDogs() != null) {
                    for (Dog dog : owner.getDogs()) {
                        if (!isAnonymized(Dog.class, dog.getId())) {
                            anonymizeEntity(Dog.class, dog.getId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'anonymisation des entités liées: " + e.getMessage());
        }
    }

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

    // ✅ Méthode modifiée pour supporter l'unicité
    private <T> void anonymizeAllFields(T entity, Class<?> entityClass, Long entityId) {
        Class<?> currentClass = entityClass;
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(PersonalData.class)) {
                    anonymizeField(entity, field, entityId); // ⭐ Passer l'ID
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private <T> void setInactiveIfExists(T entity) {
        try {
            Field isActiveField = entity.getClass().getDeclaredField("isActive");
            isActiveField.setAccessible(true);
            isActiveField.set(entity, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Le champ isActive n'existe pas, on ignore
        }
    }

    // ✅ Méthode modifiée pour gérer l'unicité
    private <T> void anonymizeField(T entity, Field field, Long entityId) {
        try {
            field.setAccessible(true);
            PersonalData annotation = field.getAnnotation(PersonalData.class);

            // ✅ Gestion spéciale des champs uniques
            if (field.getName().equalsIgnoreCase("email")) {
                long timestamp = System.currentTimeMillis();
                field.set(entity, "deleted-" + timestamp + "@example.com");
            } else if (field.getName().equalsIgnoreCase("chipNumber")) {
                // ✅ NOUVEAU : Chip number unique par chien
                field.set(entity, "000-" + String.format("%06d", entityId));
            } else {
                field.set(entity, annotation.anonymizeWith());
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Impossible d'accéder au champ pour l'anonymisation: " + field.getName(), e);
        }
    }

    private <T> void setGdprAuditFields(T entity, Long id) {
        try {
            Field isAnonymizedField = findFieldInHierarchy(entity.getClass(), "isAnonymized");
            if (isAnonymizedField != null) {
                isAnonymizedField.setAccessible(true);
                isAnonymizedField.set(entity, true);
            }

            Field anonymizedAtField = findFieldInHierarchy(entity.getClass(), "anonymizedAt");
            if (anonymizedAtField != null) {
                anonymizedAtField.setAccessible(true);
                anonymizedAtField.set(entity, LocalDateTime.now());
            }

            Field anonymizedByField = findFieldInHierarchy(entity.getClass(), "anonymizedBy");
            if (anonymizedByField != null) {
                anonymizedByField.setAccessible(true);
                String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
                anonymizedByField.set(entity, currentUser);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erreur lors de la mise à jour des champs d'audit RGPD pour l'entité avec ID: " + id, e);
        }
    }

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
            return false;
        }
    }
}