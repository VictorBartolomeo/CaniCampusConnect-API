package org.example.canicampusconnectapi.security.annotation.rgpd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PersonalData {
    /**
     * Spécifie la valeur de remplacement pour le champ lors de l'anonymisation.
     * Si non défini, le champ sera mis à null.
     */
    String anonymizeWith() default "ANONYMIZED"; // Valeur par défaut
}