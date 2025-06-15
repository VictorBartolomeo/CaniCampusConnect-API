package org.example.canicampusconnectapi.security.annotation.role;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('OWNER') and @ownerSecurityService.canAccessOwner(#id, authentication.principal.userId) or hasRole('CLUB_OWNER')")
public @interface IsOwnerSelfOrAdmin {
}
