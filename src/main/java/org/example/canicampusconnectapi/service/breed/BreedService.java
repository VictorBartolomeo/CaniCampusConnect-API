package org.example.canicampusconnectapi.service.breed;

import org.example.canicampusconnectapi.model.dogRelated.Breed;

import java.util.List;
import java.util.Optional;

public interface BreedService {
    Optional<Breed> findById(Short id);
    List<Breed> findAll();
    Breed save(Breed breed);
    void deleteById(Short id);
    boolean existsById(Short id);
    Optional<Breed> updateAvatarUrl(Short breedId, String avatarUrl);
}