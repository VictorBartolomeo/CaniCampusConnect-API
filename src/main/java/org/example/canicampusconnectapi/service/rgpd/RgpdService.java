package org.example.canicampusconnectapi.service.rgpd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.example.canicampusconnectapi.security.annotation.rgpd.PersonalData;
import org.example.canicampusconnectapi.security.annotation.rgpd.RgpdEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RgpdService {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> void anonymizeEntity(Class<T> entityClass, Object identifier) {
        if (!entityClass.isAnnotationPresent(RgpdEntity.class)) {
            throw new IllegalArgumentException("Entity must be annotated with @RgpdEntity");
        }

        RgpdEntity rgpdEntity = entityClass.getAnnotation(RgpdEntity.class);
        String identifierField = rgpdEntity.identifierField();

        List<String> updateClauses = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(PersonalData.class)) {
                PersonalData personalData = field.getAnnotation(PersonalData.class);
                String anonymizedValue = generateAnonymizedValue(field, personalData, identifier);
                updateClauses.add(field.getName() + " = '" + anonymizedValue + "'");
            }
        }

        // Ajouter les champs d'audit
        updateClauses.add("isAnonymized = true");
        updateClauses.add("anonymizedAt = '" + LocalDateTime.now() + "'");

        if (!updateClauses.isEmpty()) {
            String tableName = getTableName(entityClass);
            String sql = "UPDATE " + tableName + " SET " +
                    String.join(", ", updateClauses) +
                    " WHERE " + identifierField + " = :identifier";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("identifier", identifier);
            query.executeUpdate();
        }
    }

    private String generateAnonymizedValue(Field field, PersonalData personalData, Object identifier) {
        String baseValue = personalData.anonymizeWith();

        // Personnalisation selon le type de champ
        if (field.getType() == String.class) {
            if (field.getName().toLowerCase().contains("name")) {
                return baseValue + "_" + identifier;
            } else if (field.getName().toLowerCase().contains("email")) {
                return "anonymized_" + identifier + "@deleted.canicampus";
            } else if (field.getName().toLowerCase().contains("phone")) {
                return "+33000000000";
            } else if (field.getName().toLowerCase().contains("chip")) {
                return null;
            }
            return baseValue + "_" + identifier;
        }

        return personalData.nullable() ? null : baseValue;
    }

    private String getTableName(Class<?> entityClass) {
        // Convertit le nom de classe en nom de table (CamelCase -> snake_case)
        String className = entityClass.getSimpleName();
        return className.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public <T> boolean isAnonymized(Class<T> entityClass, Object identifier) {
        String tableName = getTableName(entityClass);
        String sql = "SELECT is_anonymized FROM " + tableName + " WHERE id = :identifier";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("identifier", identifier);

        Object result = query.getSingleResult();
        return result != null && (Boolean) result;
    }
}