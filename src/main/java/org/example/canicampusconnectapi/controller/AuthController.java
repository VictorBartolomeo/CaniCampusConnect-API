package org.example.canicampusconnectapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.canicampusconnectapi.common.exception.EmailConstraintRequests;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.dto.ChangePasswordDTO;
import org.example.canicampusconnectapi.dto.EmailValidationRequest;
import org.example.canicampusconnectapi.dto.UserLoginDto;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.SecurityUtils;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.EmailService;
import org.example.canicampusconnectapi.service.TokenService; // ⭐ VOTRE TokenService, pas celui de Spring
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
@Validated
public class AuthController {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final SecurityUtils securityUtils;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserDao userDao,
                          PasswordEncoder passwordEncoder,
                          AuthenticationProvider authenticationProvider,
                          SecurityUtils securityUtils,
                          UserService userService,
                          TokenService tokenService,
                          EmailService emailService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.securityUtils = securityUtils;
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @PostMapping("/owner/register")
    public ResponseEntity<Owner> register(@RequestBody @Validated(Owner.onCreateOwner.class) Owner owner) {
        try {
            owner.setPassword(passwordEncoder.encode(owner.getPassword()));
            userDao.save(owner);

            try {
                emailService.sendEmailValidationToken(owner.getEmail());
                System.out.println("✅ Email de validation envoyé à: " + owner.getEmail());
            } catch (Exception emailError) {
                System.err.println("❌ Erreur envoi email: " + emailError.getMessage());
            }

            owner.setPassword(null);
            return new ResponseEntity<>(owner, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLogin) {
        try {
            AppUserDetails userDetails = (AppUserDetails) authenticationProvider.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userLogin.getEmail(),
                                    userLogin.getPassword()))
                    .getPrincipal();

            return new ResponseEntity<>(securityUtils.generateToken(userDetails), HttpStatus.OK);

        } catch (AuthenticationException e) {
            if (e.getMessage().contains("Compte non activé")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "error", "Compte non activé",
                        "message", "Votre compte n'est pas encore activé. Veuillez valider votre email avant de vous connecter.",
                        "code", "EMAIL_NOT_VALIDATED",
                        "action", "validate_email"
                ));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "error", "Échec de l'authentification",
                    "message", e.getMessage()
            ));
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

    @PostMapping("/send-validation-email")
    public ResponseEntity<?> sendValidationEmail(
            @RequestBody @Validated EmailValidationRequest request) {

        try {
            // Vérifier si l'utilisateur existe
            if (!userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Aucun compte trouvé avec cet email"));
            }

            // Vérifier si le compte n'est pas déjà validé
            if (userService.isEmailAlreadyValidated(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Ce compte est déjà validé"));
            }

            // ⭐ SIMPLIFIÉ - Envoyer l'email de validation
            emailService.sendEmailValidationToken(request.getEmail());

            // Obtenir le nombre de tokens récents (pour information)
            long remainingAttempts = Math.max(0, 3 - tokenService.getRecentTokenCount(request.getEmail()));

            return ResponseEntity.ok(Map.of(
                    "message", "Email de validation envoyé avec succès",
                    "remainingAttempts", remainingAttempts
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validate-email")
    public ResponseEntity<?> validateEmail(
            @RequestParam @NotBlank(message = "Le token est obligatoire") String token,
            @RequestParam @Email(message = "Format d'email invalide") String email) {

        try {
            // Valider le token
            boolean isValidToken = tokenService.validateToken(token, email);

            if (!isValidToken) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Token invalide ou expiré",
                        "code", "INVALID_TOKEN"
                ));
            }

            boolean accountActivated = userService.activateUserAccount(email);

            if (!accountActivated) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Impossible d'activer le compte",
                        "code", "ACTIVATION_FAILED"
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Compte validé avec succès ! Vous pouvez maintenant vous connecter.",
                    "code", "VALIDATION_SUCCESS"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur interne lors de la validation",
                    "code", "INTERNAL_ERROR"
            ));
        }
    }

    @IsClubOwner
    @PostMapping("/coach/register")
    public ResponseEntity<Coach> registerCoach(@RequestBody @Validated(Coach.onCreateCoach.class) Coach coach) {
        try {
            coach.setPassword(passwordEncoder.encode(coach.getPassword()));
            userDao.save(coach);

            try {
                emailService.sendEmailValidationToken(coach.getEmail());
                System.out.println("✅ Email de validation envoyé à: " + coach.getEmail());
            } catch (Exception emailError) {
                System.err.println("❌ Erreur envoi email: " + emailError.getMessage());
            }

            coach.setPassword(null);
            return new ResponseEntity<>(coach, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/resend-validation-email")
    public ResponseEntity<?> resendValidationEmail(@RequestParam String email) {
        try {
            tokenService.resendValidationEmail(email);
            emailService.sendEmailValidationToken(email);

            return ResponseEntity.ok(Map.of(
                    "message", "Email de validation renvoyé avec succès",
                    "code", "EMAIL_RESENT"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Aucun compte trouvé pour cet email",
                    "code", "ACCOUNT_NOT_FOUND"
            ));

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Ce compte est déjà validé",
                    "code", "ALREADY_VALIDATED"
            ));

        } catch (EmailConstraintRequests e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage(),
                    "code", "RATE_LIMIT_EXCEEDED"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Erreur lors de l'envoi de l'email",
                    "code", "INTERNAL_ERROR"
            ));
        }
    }
}
