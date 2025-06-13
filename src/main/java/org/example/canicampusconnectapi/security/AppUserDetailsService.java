package org.example.canicampusconnectapi.security;

import org.example.canicampusconnectapi.dao.ClubOwnerDao;
import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.model.users.ClubOwner;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.model.users.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    protected ClubOwnerDao clubOwnerDao;
    protected OwnerDao ownerDao;
    protected CoachDao coachDao;

    @Autowired
    public AppUserDetailsService(OwnerDao ownerDao, ClubOwnerDao clubOwnerDao, CoachDao coachDao) {
        this.ownerDao = ownerDao;
        this.clubOwnerDao = clubOwnerDao;
        this.coachDao = coachDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Rechercher dans les owners
        Optional<Owner> optionalOwner = ownerDao.findByEmail(email);
        if (optionalOwner.isPresent()) {
            Owner owner = optionalOwner.get();

            if (!owner.isEmailValidated()) {
                throw new UsernameNotFoundException("Compte non activé. Veuillez valider votre email avant de vous connecter.");
            }

            return new AppUserDetails(owner);
        }

        Optional<Coach> optionalCoach = coachDao.findByEmail(email);
        if (optionalCoach.isPresent()) {
            Coach coach = optionalCoach.get();

            if (!coach.isEmailValidated()) {
                throw new UsernameNotFoundException("Compte non activé. Veuillez valider votre email avant de vous connecter.");
            }

            return new AppUserDetails(coach);
        }

        Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findByEmail(email);
        if (optionalClubOwner.isPresent()) {
            ClubOwner clubOwner = optionalClubOwner.get();

            return new AppUserDetails(clubOwner);
        }

        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
    }
}