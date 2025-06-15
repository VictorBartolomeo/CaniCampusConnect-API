package org.example.canicampusconnectapi.service.security;

import org.example.canicampusconnectapi.security.AppUserDetails;
import org.springframework.stereotype.Service;

@Service
public class OwnerSecurityService {

    public boolean canAccessOwner(Long requestedOwnerId, Long currentUserId) {
        // Un owner ne peut accéder qu'à ses propres données
        return requestedOwnerId.equals(currentUserId);
    }

    public boolean isOwnerSelfOrAdmin(Long requestedOwnerId, AppUserDetails userDetails) {
        // Si c'est un admin (ClubOwner), autoriser
        if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLUB_OWNER"))) {
            return true;
        }

        // Si c'est un owner, vérifier que c'est le bon
        if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_OWNER"))) {
            return requestedOwnerId.equals(userDetails.getUserId());
        }

        return false;
    }
}