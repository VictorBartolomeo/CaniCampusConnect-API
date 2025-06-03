package org.example.canicampusconnectapi.service.breed;

import org.example.canicampusconnectapi.model.dogRelated.Breed;
import java.util.Optional;

public interface BreedService {

    Optional<Breed> updateAvatarUrl(Short breedId, String avatarUrl);
    Optional<Breed> findById(Short id);
    Optional<Breed> findByName(String name);
}