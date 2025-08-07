package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.common.exception.RgpdAlreadyAnonymizedException;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwnerSelfOrAdmin;
import org.example.canicampusconnectapi.service.user.UserService;
import org.example.canicampusconnectapi.view.admin.AdminViewCoach;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDao userDao;
    private final String avatarDir = "./uploads/users/";

    /**
     * Récupère tous les utilisateurs non anonymisés sans pagination
     */
    @GetMapping("/users")
    @JsonView(AdminViewCoach.class)
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersNotAnonymized());
    }

    /**
     * Récupère tous les coaches non anonymisés
     */
    @GetMapping("/users/coaches")
    @JsonView(AdminViewCoach.class)
    public ResponseEntity<?> getAllCoaches() {
        return ResponseEntity.ok(userService.getAllCoachesNotAnonymized());
    }

    /**
     * Récupère tous les propriétaires non anonymisés
     */
    @GetMapping("/users/owners")
    @JsonView(AdminViewCoach.class)
    public ResponseEntity<?> getAllOwners() {
        return ResponseEntity.ok(userService.getAllOwnersNotAnonymized());
    }

    /**
     * Récupère les statistiques des utilisateurs (excluant les anonymisés)
     */
    @GetMapping("/users/stats")
    public ResponseEntity<UserService.UserStatsDto> getUserStats() {
        return ResponseEntity.ok(userService.getUserStats());
    }

    /**
     * Recherche d'utilisateurs non anonymisés par email ou nom
     */
    @GetMapping("/users/search")
    @JsonView(AdminViewCoach.class)
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchUsersNotAnonymized(query));
    }

    /**
     * Récupère un utilisateur par ID (seulement si non anonymisé)
     */
    @GetMapping("/user/{id}")
    @JsonView(AdminViewCoach.class)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserByIdIfNotAnonymized(id)
                .map(user -> ResponseEntity.ok((Object) user))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body((Object) Map.of("error", "Utilisateur introuvable ou anonymisé")));
    }

    /**
     * Récupère tous les utilisateurs avec leurs rôles (inclut les anonymisés pour l'admin)
     */
    @GetMapping("/users/with-roles")
    public ResponseEntity<List<UserService.UserWithRoleDto>> getAllUsersWithRoles() {
        return ResponseEntity.ok(userService.getAllUsersWithRoles());
    }

    @PutMapping("/user/{id}")
    @JsonView(AdminViewCoach.class)
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody @Validated(User.OnUpdateFromAdmin.class) User user) {
        try {
            User existingUser = userDao.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            existingUser.setEmail(user.getEmail());
            existingUser.setFirstname(user.getFirstname());
            existingUser.setLastname(user.getLastname());
            existingUser.setPhone(user.getPhone());
            existingUser.setAvatarUrl(user.getAvatarUrl());

            User updatedUser = userDao.save(existingUser);

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    /**
     * Anonymise un utilisateur (conforme RGPD) - Ne supprime pas mais rend anonyme
     */
    @IsOwnerSelfOrAdmin
    @Transactional
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Map<String, String>> anonymizeUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails userDetails) {

        try {
            userService.anonymizeUser(id, userDetails.getUsername());

            return ResponseEntity.ok(Map.of(
                    "message", "Utilisateur anonymisé avec succès (conformité RGPD)",
                    "userId", id.toString(),
                    "anonymizedBy", userDetails.getUsername()
            ));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();

        } catch (RgpdAlreadyAnonymizedException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "already_anonymized",
                    "message", e.getMessage()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "anonymization_failed",
                    "message", "Erreur lors de l'anonymisation: " + e.getMessage()
            ));
        }
    }

    /**
     * Avatar utilisateur - Accès public mais refuse les utilisateurs anonymisés
     */
    @Deprecated
    @IsOwner
    @GetMapping("/user/{id}/avatar")
    public ResponseEntity<Resource> getUserAvatar(@PathVariable Long id) {
        try {
            // Vérifier si l'utilisateur existe et n'est pas anonymisé
            if (userService.isUserAnonymized(id)) {
                return ResponseEntity.notFound().build();
            }

            if (userService.getUserByIdIfNotAnonymized(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Chercher le fichier avec les extensions courantes
            String[] extensions = {"png", "jpg", "jpeg", "webp"};
            for (String ext : extensions) {
                String fileName = id + "." + ext;
                Path filePath = Paths.get(avatarDir).resolve(fileName);

                if (Files.exists(filePath)) {
                    Resource resource = new UrlResource(filePath.toUri());

                    if (resource.exists() && resource.isReadable()) {
                        String contentType = Files.probeContentType(filePath);
                        if (contentType == null) {
                            contentType = "image/png";
                        }

                        return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(contentType))
                                .body(resource);
                    }
                }
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}