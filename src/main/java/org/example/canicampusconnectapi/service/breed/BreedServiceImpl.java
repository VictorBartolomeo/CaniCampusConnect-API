
package org.example.canicampusconnectapi.service.breed;

import jakarta.transaction.Transactional;
import org.example.canicampusconnectapi.dao.BreedDao;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BreedServiceImpl implements BreedService {

    private final BreedDao breedDao;

    public BreedServiceImpl(BreedDao breedDao) {
        this.breedDao = breedDao;
    }

    @Override
    public Optional<Breed> findById(Short id) {
        return breedDao.findByIdWithDogs(id);
    }

    @Override
    public List<Breed> findAll() {
        return breedDao.findAllWithDogs();
    }

    @Override
    public Breed save(Breed breed) {
        return breedDao.save(breed);
    }

    @Override
    public void deleteById(Short id) {
        breedDao.deleteById(id);
    }

    @Override
    public boolean existsById(Short id) {
        return breedDao.existsById(id);
    }

    @Override
    public Optional<Breed> updateAvatarUrl(Short breedId, String avatarUrl) {
        return breedDao.findById(breedId)
                .map(breed -> {
                    breed.setAvatarUrl(avatarUrl);
                    return breedDao.save(breed);
                });
    }
}