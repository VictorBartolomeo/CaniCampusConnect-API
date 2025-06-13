package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.users.EmailValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailValidationTokenDao extends JpaRepository<EmailValidationToken, Long> {

    Optional<EmailValidationToken> findByTokenAndEmail(String token, String email);

    List<EmailValidationToken> findByEmailAndUsedFalse(String email);

    @Query("SELECT COUNT(t) FROM EmailValidationToken t WHERE t.email = :email AND t.createdAt > :since")
    long countRecentTokensByEmail(@Param("email") String email, @Param("since") LocalDateTime since);

    /**
     * ⭐ NOUVEAU - Récupère les emails ayant des tokens expirés
     */
    @Query("SELECT DISTINCT t.email FROM EmailValidationToken t WHERE t.expiresAt < :now")
    List<String> findEmailsWithExpiredTokens(@Param("now") LocalDateTime now);

    /**
     * ⭐ NETTOYAGE automatique des tokens expirés
     */
    @Modifying
    @Query("DELETE FROM EmailValidationToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}