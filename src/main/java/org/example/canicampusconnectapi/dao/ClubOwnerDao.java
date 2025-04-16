package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.ClubOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClubOwnerDao extends JpaRepository<ClubOwner, Long> {

    Optional<ClubOwner> findByEmail(String email);

}