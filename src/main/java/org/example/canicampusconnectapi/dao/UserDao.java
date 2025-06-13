package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.users.User;
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
}