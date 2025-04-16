package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CoachDao extends JpaRepository<Coach, Long> {
    Optional<Coach> findByEmail(String email);
}