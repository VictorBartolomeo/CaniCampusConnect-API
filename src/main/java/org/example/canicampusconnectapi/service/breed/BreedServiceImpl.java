
package org.example.canicampusconnectapi.service.breed;

import jakarta.transaction.Transactional;
import org.example.canicampusconnectapi.dao.BreedDao;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class BreedServiceImpl implements BreedService {

    private final BreedDao breedDao;

    // Constructeur pour l'injection de dépendance
    @Autowired
    public BreedServiceImpl(BreedDao breedDao) {
        this.breedDao = breedDao;
    }

    public Optional<Breed> updateAvatarUrl(Short breedId, String avatarUrl) {
        return breedDao.findById(breedId)
                .map(breed -> {
                    breed.setAvatarUrl(avatarUrl);
                    return breedDao.save(breed);
                });
    }
}