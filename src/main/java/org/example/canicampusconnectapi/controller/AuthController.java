package org.example.canicampusconnectapi.controller;

import jakarta.validation.Valid;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.User;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    protected JwtUtils jwtUtils;

    @Autowired
    public AuthController(UserDao userDao, PasswordEncoder passwordEncoder, AuthenticationProvider authenticationProvider, JwtUtils jwtUtils) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.jwtUtils = jwtUtils;
    }

//TODO Demander à Franck comment faire pour ajouter les informations d'un Owner directement par exemple, ou d'un coach
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        //Masque le mot de passe dans la réponse
        user.setPassword(null);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid User user) {
        try {
            AppUserDetails userDetails = (AppUserDetails) authenticationProvider.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    user.getPassword()))
                    .getPrincipal();
            return new ResponseEntity<>(jwtUtils.generateToken(userDetails), HttpStatus.OK);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }


}
