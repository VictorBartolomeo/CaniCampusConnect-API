package org.example.canicampusconnectapi.security;

import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.dao.ClubOwnerDao;
import org.example.canicampusconnectapi.model.Owner;
import org.example.canicampusconnectapi.model.ClubOwner;
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

    @Autowired
    public AppUserDetailsService(OwnerDao ownerDao, ClubOwnerDao clubOwnerDao) {
        this.ownerDao = ownerDao;
        this.clubOwnerDao = clubOwnerDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        Optional<Owner> optionalOwner = ownerDao.findByEmail(email);


        if (optionalOwner.isEmpty()) {
            Optional<ClubOwner> optionalClubOwner = clubOwnerDao.findByEmail(email);

            if (optionalClubOwner.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }
            else {
                return new AppUserDetails(optionalClubOwner.get());
            }
        }
        else {
            return new AppUserDetails(optionalOwner.get());
        }
    }
}
