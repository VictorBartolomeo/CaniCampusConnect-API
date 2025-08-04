package org.example.canicampusconnectapi.service.user;

import lombok.RequiredArgsConstructor;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.common.exception.RgpdAlreadyAnonymizedException;
import org.example.canicampusconnectapi.dao.ClubOwnerDao;
import org.example.canicampusconnectapi.dao.CoachDao;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.dao.UserDao;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.model.users.User;
import org.example.canicampusconnectapi.service.rgpd.RgpdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ClubOwnerDao clubOwnerDao;
    private final CoachDao coachDao;
    private final OwnerDao ownerDao;
    private final RgpdService rgpdService;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean isUserAnonymized(Long id) {
        return rgpdService.isAnonymized(User.class, id);
    }

    @Override
    public Optional<User> updateAvatarUrl(Long userId, String avatarUrl) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAvatarUrl(avatarUrl);
            User savedUser = userDao.save(user);
            return Optional.of(savedUser);
        }
        return Optional.empty();
    }

    @Override
    public boolean verifyCurrentPassword(Long userId, String password) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            return passwordEncoder.matches(password, userOptional.get().getPassword());
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.findByEmail(email).isPresent();
    }

    @Override
    public boolean isEmailAlreadyValidated(String email) {
        Optional<User> userOptional = userDao.findByEmail(email);
        return userOptional.map(User::isEmailValidated).orElse(false);
    }

    @Override
    public boolean activateUserAccount(String email) {
        Optional<User> userOptional = userDao.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmailValidated(true);
            user.setEmailValidatedAt(java.time.LocalDateTime.now());
            userDao.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        if (!verifyCurrentPassword(userId, currentPassword)) {
            return false;
        }

        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userDao.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsersNotAnonymized() {
        return userDao.findAllNotAnonymized();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coach> getAllCoachesNotAnonymized() {
        return coachDao.findAllNotAnonymized();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Owner> getAllOwnersNotAnonymized() {
        return ownerDao.findAllNotAnonymized();
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatsDto getUserStats() {
        long totalUsers = userDao.countNotAnonymized();
        long totalCoaches = coachDao.countNotAnonymized();
        long totalOwners = ownerDao.countNotAnonymized();
        long validatedUsers = userDao.countByEmailValidatedAndNotAnonymized(true);
        long unvalidatedUsers = userDao.countByEmailValidatedAndNotAnonymized(false);
        long anonymizedUsers = userDao.countAnonymized();

        return new UserStatsDto(
                totalUsers,
                totalCoaches,
                totalOwners,
                validatedUsers,
                unvalidatedUsers,
                anonymizedUsers
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsersNotAnonymized(String query) {
        return userDao.findByEmailContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseAndNotAnonymized(
                query, query, query
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByIdIfNotAnonymized(Long id) {
        if (isUserAnonymized(id)) {
            return Optional.empty();
        }
        return userDao.findById(id);
    }


    @Override
    public void anonymizeUser(Long id, String anonymizedBy) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));

        if (rgpdService.isAnonymized(User.class, id)) {
            throw new RgpdAlreadyAnonymizedException("User", id);
        }
        if (user instanceof Coach) {
            rgpdService.anonymizeEntity(Coach.class, id);
            logger.info("Coach ID {} anonymisé avec succès par {}", id, anonymizedBy);
        } else {
            rgpdService.anonymizeEntity(User.class, id);
            logger.info("Utilisateur ID {} anonymisé avec succès par {}", id, anonymizedBy);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<UserWithRoleDto> getAllUsersWithRoles() {
        List<User> allUsers = userDao.findAllNotAnonymized();

        return allUsers.stream()
                .map(user -> new UserWithRoleDto(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getPhone(),
                        user.getAvatarUrl(),
                        user.isEmailValidated(),
                        determineUserRole(user),
                        determineUserRoleDisplayName(user),
                        user.isAnonymized()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public String determineUserRole(User user) {
        if (clubOwnerDao.existsById(user.getId())) {
            return "ROLE_CLUB_OWNER";
        }

        // Ensuite Coach
        if (coachDao.existsById(user.getId())) {
            return "ROLE_COACH";
        }

        // Ensuite Owner
        else {
            return "ROLE_OWNER";
        }


    }

    /**
     * Retourne le nom d'affichage du rôle pour l'interface utilisateur
     */
    private String determineUserRoleDisplayName(User user) {
        return switch (determineUserRole(user)) {
            case "ROLE_CLUB_OWNER" -> "Propriétaire de Club";
            case "ROLE_COACH" -> "Coach";
            case "ROLE_OWNER" -> "Propriétaire";
            default -> "Inconnu";
        };
    }

}