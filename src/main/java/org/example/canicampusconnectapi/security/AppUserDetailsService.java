package org.example.canicampusconnectapi.security;

import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.dao.ClubOwnerDao;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.ClubOwner;
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

        System.out.println("Tentative de chargement de l'utilisateur avec email : {$email}"); // Log l'email reçu
        Optional<Owner> optionalOwner = ownerDao.findByEmail(email);

        if (optionalOwner.isEmpty()) {
            System.out.println("Non trouvé Owner : " + email);
            Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findByEmail(email);

            if (optionalClubOwner.isEmpty()) {
                System.out.println("Non trouvé Club Owner : " + email);
                Optional<Coach> optionalCoach = coachDao.findByEmail(email);

                if (optionalCoach.isEmpty()) {
                    System.out.println("Non trouvé Coach : " + email);
                    throw new UsernameNotFoundException("User not found");
                } else {
                    return new AppUserDetails(optionalCoach.get());
                }
            } else {
                return new AppUserDetails(optionalClubOwner.get());
            }
        } else {
            return new AppUserDetails(optionalOwner.get());
        }
    }
}
