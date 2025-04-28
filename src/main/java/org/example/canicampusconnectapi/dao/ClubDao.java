package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Club;
import org.example.canicampusconnectapi.model.ClubOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubDao extends JpaRepository<Club, Integer> {
    List<Club> findByClubOwner(ClubOwner clubOwner);
    Optional<Club> findByName(String name);
}