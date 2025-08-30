
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

    default int getPriority() {
        return 100;
    }
}