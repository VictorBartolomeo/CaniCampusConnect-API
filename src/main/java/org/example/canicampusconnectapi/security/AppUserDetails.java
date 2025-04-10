package org.example.canicampusconnectapi.security;

import org.example.canicampusconnectapi.model.Coach;
import org.example.canicampusconnectapi.model.Owner;
import org.example.canicampusconnectapi.model.ClubOwner;
import org.example.canicampusconnectapi.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AppUserDetails implements UserDetails {

    protected User user;

    public AppUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

            if (user instanceof Owner) {

                return List.of(new SimpleGrantedAuthority("ROLE_OWNER"));
            }
            else if (user instanceof ClubOwner) {
                return List.of(new SimpleGrantedAuthority("ROLE_CLUB_OWNER"));
            }
            else if (user instanceof Coach) {
                return List.of(new SimpleGrantedAuthority("ROLE_COACH"));
            }
            else {
                return Collections.emptyList();
            }
        }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
