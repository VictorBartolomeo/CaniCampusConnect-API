package org.example.canicampusconnectapi.security.annotation.rgpd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RgpdEntity {
    /**
     * Le nom du champ qui sert d'identifiant (ex: "id").
     */
    String identifierField();
}