package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BreedDao extends JpaRepository<Breed, Short> {
    Optional<Breed> findByName(String name);

    @Query("SELECT b FROM Breed b LEFT JOIN FETCH b.dogs WHERE b.id = :id")
    Optional<Breed> findByIdWithDogs(Short id);

    @Query("SELECT DISTINCT b FROM Breed b LEFT JOIN FETCH b.dogs")
    List<Breed> findAllWithDogs();

}
