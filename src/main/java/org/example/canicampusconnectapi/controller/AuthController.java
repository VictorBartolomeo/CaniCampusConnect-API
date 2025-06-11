package org.example.canicampusconnectapi.controller;

import jakarta.validation.Valid;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.dto.ChangePasswordDTO;
import org.example.canicampusconnectapi.dto.UserLoginDto;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.SecurityUtils;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
public class AuthController {

    protected UserDao userDao;
    protected PasswordEncoder passwordEncoder;
    protected AuthenticationProvider authenticationProvider;
    protected SecurityUtils securityUtils;
    protected UserService userService;

    @Autowired
    public AuthController(UserDao userDao,
                          PasswordEncoder passwordEncoder,
                          AuthenticationProvider authenticationProvider,
                          SecurityUtils securityUtils,
                          UserService userService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    //TODO Demander à Franck comment faire pour ajouter les informations d'un Owner directement
    @PostMapping("/owner/register")
    public ResponseEntity<Owner> register(@RequestBody @Validated(Owner.onCreateOwner.class) Owner owner) {
        try {
            owner.setPassword(passwordEncoder.encode(owner.getPassword()));
            userDao.save(owner);
            //Masque le mot de passe dans la réponse
            System.out.println(owner.getPassword());
            owner.setPassword(null);
            return new ResponseEntity<>(owner, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
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
    @IsOwner
    @PutMapping(value = "/change-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(
            @RequestBody @Validated(User.OnUpdatePassword.class) ChangePasswordDTO request,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        try {
            boolean success = userService.changePassword(
                    userDetails.getUserId(),
                    request.getCurrentPassword(),
                    request.getNewPassword()
            );

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "message", "Mot de passe mis à jour avec succès"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Mot de passe actuel incorrect"
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur interne du serveur"
            ));
        }
    }
}
