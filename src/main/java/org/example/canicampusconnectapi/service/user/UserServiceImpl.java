package org.example.canicampusconnectapi.service.user;

import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.service.rgpd.RgpdService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final RgpdService rgpdService;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(RgpdService rgpdService, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.rgpdService = rgpdService;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        rgpdService.anonymizeEntity(User.class, id);
    }

    @Override
    public boolean isUserAnonymized(Long id) {
        return rgpdService.isAnonymized(User.class, id);
    }

    /**
     * Updates the avatar URL for a user.
     * @param userId The ID of the user.
     * @param avatarUrl The new avatar URL.
     * @return An Optional containing the updated user if found, otherwise empty.
     */
    @Override
    @Transactional
    public Optional<User> updateAvatarUrl(Long userId, String avatarUrl) {
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAvatarUrl(avatarUrl);
            User updatedUser = userDao.save(user);
            return Optional.of(updatedUser);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();

        // Vérifier que le mot de passe actuel est correct
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        // Encoder et sauvegarder le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.save(user);

        return true;
    }

    @Override
    public boolean verifyCurrentPassword(Long userId, String password) {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        return passwordEncoder.matches(password, user.getPassword());
    }


}