package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BreedDao extends JpaRepository<Breed, Short> {
    Optional<Breed> findByName(String name);
}