
package org.example.canicampusconnectapi.security.annotation.role;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ROLE_CLUB_OWNER') or (hasRole('ROLE_OWNER') and @securityConfig.isOwnerOfResource(authentication, #id))")
public @interface IsOwnerSelf {
}