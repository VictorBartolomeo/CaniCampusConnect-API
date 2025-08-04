package org.example.canicampusconnectapi.service.user;

import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    boolean isUserAnonymized(Long id);

    Optional<User> updateAvatarUrl(Long userId, String avatarUrl);

    boolean verifyCurrentPassword(Long userId, String password);
    /**
     * Vérifie si un utilisateur existe avec cet email
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si l'email est déjà validé
     */
    boolean isEmailAlreadyValidated(String email);

    /**
     * Active le compte utilisateur
     */
    boolean activateUserAccount(String email);

    /**
     * Change le mot de passe de l'utilisateur
     */
    boolean changePassword(Long userId, String currentPassword, String newPassword);

    /**
     * Récupère tous les utilisateurs non anonymisés
     */
    List<User> getAllUsersNotAnonymized();

    /**
     * Récupère tous les coaches non anonymisés
     */
    List<Coach> getAllCoachesNotAnonymized();

    /**
     * Récupère tous les owners non anonymisés
     */
    List<Owner> getAllOwnersNotAnonymized();

    /**
     * Récupère les statistiques des utilisateurs
     */
    UserStatsDto getUserStats();

    /**
     * Recherche des utilisateurs non anonymisés par email/nom/prénom
     */
    List<User> searchUsersNotAnonymized(String query);

    /**
     * Récupère un utilisateur par ID seulement s'il n'est pas anonymisé
     */
    Optional<User> getUserByIdIfNotAnonymized(Long id);

    /**
     * Anonymise un utilisateur (conforme RGPD)
     */
    void anonymizeUser(Long id, String anonymizedBy);

    /**
     * Récupère tous les utilisateurs avec leurs rôles détectés automatiquement
     */
    List<UserWithRoleDto> getAllUsersWithRoles();

    /**
     * Détermine le rôle d'un utilisateur basé sur son type d'entité
     */
    String determineUserRole(User user);

    /**
     * DTO pour un utilisateur avec son rôle
     */
    record UserWithRoleDto(
            Long id,
            String email,
            String firstname,
            String lastname,
            String phone,
            String avatarUrl,
            boolean emailValidated,
            String role,
            String roleDisplayName,
            boolean isAnonymized
    ) {}


    /**
     * DTO pour les statistiques utilisateurs
     */
    record UserStatsDto(
            long totalUsers,
            long totalCoaches,
            long totalOwners,
            long validatedUsers,
            long unvalidatedUsers,
            long anonymizedUsers
    ) {}



}