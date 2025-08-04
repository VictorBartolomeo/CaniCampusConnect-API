
package org.example.canicampusconnectapi.service.rgpd.strategy;

import java.lang.reflect.Field;

/**
 * Interface pour les stratégies d'anonymisation des champs
 */
public interface AnonymizationStrategy {

    /**
     * Détermine si cette stratégie peut gérer le champ donné
     */
    boolean canHandle(Field field);

    /**
     * Anonymise la valeur du champ
     */
    Object anonymize(Field field, Object entity, Long entityId);

    /**
     * Priorité de la stratégie (plus le nombre est petit, plus la priorité est élevée)
     */
    default int getPriority() {
        return 100;
    }
}