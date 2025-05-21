package org.example.canicampusconnectapi.controller;

import jakarta.validation.Valid;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.dto.OwnerRegisterDto;
import org.example.canicampusconnectapi.dto.UserLoginDto;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AuthController {

    protected UserDao userDao;
    protected PasswordEncoder passwordEncoder;
    protected AuthenticationProvider authenticationProvider;
    protected SecurityUtils securityUtils;

    @Autowired
    public AuthController(UserDao userDao, PasswordEncoder passwordEncoder, AuthenticationProvider authenticationProvider, SecurityUtils securityUtils) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.securityUtils = securityUtils;
    }

//TODO Demander à Franck comment faire pour ajouter les informations d'un Owner directement
    @PostMapping("/owner/register")
    public ResponseEntity<Owner> register(@RequestBody @Validated(Owner.onCreateOwner.class) Owner owner ) {
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        userDao.save(owner);
        //Masque le mot de passe dans la réponse
        System.out.println(owner.getPassword());
        owner.setPassword(null);
        return new ResponseEntity<>(owner, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        //Masque le mot de passe dans la réponse
        System.out.println(user.getPassword());
        user.setPassword(null);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto userLogin) {
        try {
            AppUserDetails userDetails = (AppUserDetails) authenticationProvider.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userLogin.getEmail(),
                                    userLogin.getPassword()))
                    .getPrincipal();
            System.out.println(securityUtils.generateToken(userDetails));
            return new ResponseEntity<>(securityUtils.generateToken(userDetails), HttpStatus.OK);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }


}
