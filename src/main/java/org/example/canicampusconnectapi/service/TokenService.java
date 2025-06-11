
package org.example.canicampusconnectapi.service;

import org.example.canicampusconnectapi.dao.EmailValidationTokenDao;
import org.example.canicampusconnectapi.common.exception.EmailConstraintRequests;
import org.example.canicampusconnectapi.model.users.EmailValidationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TokenService {

    private final EmailValidationTokenDao tokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.security.token.expiration-hours:24}")
    private int expirationHours;

    @Value("${app.security.token.max-emails-per-hour:3}")
    private int maxEmailsPerHour;

    public TokenService(EmailValidationTokenDao tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * ⭐ CORRIGÉ - Génère un nouveau token de validation pour l'email donné (1 seul paramètre)
     */
    public String generateValidationToken(String email) {
        // Vérifier la limitation de taux
        if (!canSendEmail(email)) {
            throw new EmailConstraintRequests(
                    String.format("Trop de demandes d'email envoyées. Maximum %d par heure.", maxEmailsPerHour)
            );
        }

        // Invalider les anciens tokens non utilisés
        invalidateOldTokens(email);

        // Générer un token cryptographiquement sûr
        String token = generateSecureToken();

        // Créer et sauvegarder le token
        EmailValidationToken validationToken = new EmailValidationToken();
        validationToken.setToken(token);
        validationToken.setEmail(email);
        validationToken.setCreatedAt(LocalDateTime.now());
        validationToken.setExpiresAt(LocalDateTime.now().plusHours(expirationHours));
        // ⭐ SUPPRIMÉ : ipAddress et userAgent

        tokenRepository.save(validationToken);
        return token;
    }

    /**
     * Valide un token et le marque comme utilisé si valide
     */
    public boolean validateToken(String token, String email) {
        Optional<EmailValidationToken> tokenOpt = tokenRepository.findByTokenAndEmail(token, email);

        if (tokenOpt.isPresent()) {
            EmailValidationToken validationToken = tokenOpt.get();
            if (validationToken.isValid()) {
                validationToken.markAsUsed();
                tokenRepository.save(validationToken);
                return true;
            }
        }
        return false;
    }

    /**
     * Génère un token cryptographiquement sécurisé
     */
    private String generateSecureToken() {
        byte[] tokenBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /**
     * Invalide tous les anciens tokens non utilisés pour un email
     */
    private void invalidateOldTokens(String email) {
        List<EmailValidationToken> oldTokens = tokenRepository.findByEmailAndUsedFalse(email);
        oldTokens.forEach(EmailValidationToken::markAsUsed);
        tokenRepository.saveAll(oldTokens);
    }

    /**
     * Vérifie si on peut envoyer un email (limitation de taux)
     */
    private boolean canSendEmail(String email) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentCount = tokenRepository.countRecentTokensByEmail(email, oneHourAgo);
        return recentCount < maxEmailsPerHour;
    }

    /**
     * Obtient le nombre de tokens récents pour un email
     */
    public long getRecentTokenCount(String email) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        return tokenRepository.countRecentTokensByEmail(email, oneHourAgo);
    }

    /**
     * Nettoyage automatique des tokens expirés (toutes les heures)
     */
    @Scheduled(fixedRate = 3600000) // 1 heure
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}