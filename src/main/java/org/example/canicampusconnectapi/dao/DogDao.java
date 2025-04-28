package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DogDao extends JpaRepository<Dog, Long> {
    List<Dog> findByName(String name);
    Optional<Dog> findByChipNumber(String chipNumber);
}
