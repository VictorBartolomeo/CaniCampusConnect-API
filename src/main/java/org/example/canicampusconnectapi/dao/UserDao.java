package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);

    /**
     * ⭐ NOUVEAU - Supprime les comptes non validés pour les emails donnés
     */
    @Modifying
    @Query("DELETE FROM User u WHERE u.email IN :emails AND u.emailValidated = false")
    int deleteUnvalidatedUsers(@Param("emails") List<String> emails);

    /**
     * ✅ NOUVEAU - Compte les utilisateurs par statut de validation email
     */
    long countByEmailValidated(boolean emailValidated);

    /**
     * Recherche par email, prénom ou nom
     */
    List<User> findByEmailContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(
            String email, String firstname, String lastname
    );

    /**
     * Trouve tous les utilisateurs non anonymisés (pagination)
     */
    @Query("SELECT u FROM User u WHERE u.isAnonymized = false")
    Page<User> findAllNotAnonymized(Pageable pageable);

    /**
     * Trouve tous les utilisateurs non anonymisés (liste complète)
     */
    @Query("SELECT u FROM User u WHERE u.isAnonymized = false")
    List<User> findAllNotAnonymized();

    /**
     * Compte les utilisateurs non anonymisés
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isAnonymized = false")
    long countNotAnonymized();

    /**
     * Compte les utilisateurs anonymisés
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isAnonymized = true")
    long countAnonymized();

    /**
     * Compte par statut de validation email (excluant les anonymisés)
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.emailValidated = :emailValidated AND u.isAnonymized = false")
    long countByEmailValidatedAndNotAnonymized(@Param("emailValidated") boolean emailValidated);

    /**
     * Recherche par email/prénom/nom (excluant les anonymisés)
     */
    @Query("SELECT u FROM User u WHERE u.isAnonymized = false AND " +
            "(LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) OR " +
            "LOWER(u.firstname) LIKE LOWER(CONCAT('%', :firstname, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :lastname, '%')))")
    List<User> findByEmailContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseAndNotAnonymized(
            @Param("email") String email,
            @Param("firstname") String firstname,
            @Param("lastname") String lastname
    );
}