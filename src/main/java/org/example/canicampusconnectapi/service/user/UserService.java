package org.example.canicampusconnectapi.service.user;

import org.example.canicampusconnectapi.model.users.User;

import java.util.Optional;

public interface UserService {
    void deleteUser(Long id);
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


}