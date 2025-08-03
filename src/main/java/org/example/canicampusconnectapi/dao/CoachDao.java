package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.users.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CoachDao extends JpaRepository<Coach, Long> {
    Optional<Coach> findByEmail(String email);

    @Query("SELECT c FROM Coach c WHERE c.isAnonymized = false")
    List<Coach> findAllNotAnonymized();

    @Query("SELECT COUNT(c) FROM Coach c WHERE c.isAnonymized = false")
    long countNotAnonymized();

}