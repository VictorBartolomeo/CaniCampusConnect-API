package org.example.canicampusconnectapi.service.rgpd;

import jakarta.persistence.EntityManager;
import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.security.annotation.rgpd.PersonalData;
import org.example.canicampusconnectapi.security.annotation.rgpd.RgpdEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (!entityClass.isAnnotationPresent(RgpdEntity.class)) {
            throw new IllegalArgumentException("L'entité " + entityClass.getSimpleName() + " n'est pas marquée comme @RgpdEntity.");
        }

        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            throw new ResourceNotFound("Entité non trouvée avec l'ID: " + id);
        }

        // Anonymiser les champs personnels
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PersonalData.class)) {
                anonymizeField(entity, field);
            }
        }

        setGdprAuditFields(entity, id);

        entityManager.merge(entity);
    }

    private <T> void anonymizeField(T entity, Field field) {
        try {
            field.setAccessible(true);
            PersonalData annotation = field.getAnnotation(PersonalData.class);

            // Gère le cas spécial du champ email pour garantir l'unicité
            if (field.getName().equalsIgnoreCase("email")) {
                long timestamp = System.currentTimeMillis();
                field.set(entity, "deleted-" + timestamp + "@example.com");
            } else {
                field.set(entity, annotation.anonymizeWith());
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Impossible d'accéder au champ pour l'anonymisation: " + field.getName(), e);
        }
    }

    private <T> void setGdprAuditFields(T entity, Long id) {
        try {
            // Marquer comme anonymisé
            Field isAnonymizedField = entity.getClass().getDeclaredField("isAnonymized");
            isAnonymizedField.setAccessible(true);
            isAnonymizedField.set(entity, true);

            // Date d'anonymisation
            Field anonymizedAtField = entity.getClass().getDeclaredField("anonymizedAt");
            anonymizedAtField.setAccessible(true);
            anonymizedAtField.set(entity, LocalDateTime.now());

            // Qui a anonymisé
            Field anonymizedByField = entity.getClass().getDeclaredField("anonymizedBy");
            anonymizedByField.setAccessible(true);
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            anonymizedByField.set(entity, currentUser);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Erreur lors de la mise à jour des champs d'audit RGPD pour l'entité avec ID: " + id, e);
        }
    }

    public <T> boolean isAnonymized(Class<T> entityClass, Long id) {
        T entity = entityManager.find(entityClass, id);
        if (entity == null) {
            return false;
        }
        try {
            Field isAnonymizedField = entityClass.getDeclaredField("isAnonymized");
            isAnonymizedField.setAccessible(true);
            return (boolean) isAnonymizedField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}