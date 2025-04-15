package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // @Component ou @Service
public interface UserDao extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
