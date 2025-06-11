package org.example.canicampusconnectapi.service.user;

import org.example.canicampusconnectapi.model.users.User;

import java.util.Optional;

public interface UserService {
    void deleteUser(Long id);
    boolean isUserAnonymized(Long id);
    Optional<User> updateAvatarUrl(Long userId, String avatarUrl);
    boolean changePassword(Long userId, String currentPassword, String newPassword);
    boolean verifyCurrentPassword(Long userId, String password);


}