package org.example.canicampusconnectapi.common.exception;

/**
 * Exception levée quand on tente d'anonymiser une entité déjà anonymisée
 */
public class RgpdAlreadyAnonymizedException extends RuntimeException {

    public RgpdAlreadyAnonymizedException(String entityType, Long entityId) {
        super(String.format("L'entité %s (ID: %d) est déjà anonymisée", entityType, entityId));
    }
}