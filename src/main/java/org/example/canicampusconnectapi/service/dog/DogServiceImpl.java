package org.example.canicampusconnectapi.service.dog;

import jakarta.transaction.Transactional;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.common.exception.UnauthorizedAccessException;
import org.example.canicampusconnectapi.dao.BreedDao;
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.service.rgpd.RgpdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DogServiceImpl implements DogService {

    private final DogDao dogDao;
    private final OwnerDao ownerDao;
    private final BreedDao breedDao;
    private final RgpdService rgpdService;

    @Autowired
    public DogServiceImpl(DogDao dogDao, OwnerDao ownerDao, BreedDao breedDao, RgpdService rgpdService) {
        this.dogDao = dogDao;
        this.ownerDao = ownerDao;
        this.breedDao = breedDao;
        this.rgpdService = rgpdService;
    }

    @Override
    public Optional<Dog> getDogById(Long id) {
        return dogDao.findById(id);
    }

    @Override
    public List<Dog> getAllDogs() {
        return dogDao.findAll();
    }

    @Override
    public List<Dog> getDogsByOwner(Long ownerId) {
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId));
        return dogDao.findByOwner(owner);
    }

    @Override
    @Transactional
    public Dog createDog(Dog dog) {
        if (dog.getOwner() == null || dog.getOwner().getId() == null) {
            throw new IllegalArgumentException("Owner ID must be provided to create a dog.");
        }

        Owner owner = ownerDao.findById(dog.getOwner().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + dog.getOwner().getId()));

        dog.setOwner(owner);
        dog.setId(null);

        // ✅ Valider et traiter les races si nécessaire
        if (dog.getBreeds() != null && !dog.getBreeds().isEmpty()) {
            List<Breed> validatedBreeds = new ArrayList<>();

            for (Breed breed : dog.getBreeds()) {
                if (breed.getId() != null) {
                    Breed existingBreed = breedDao.findById(breed.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Race non trouvée avec l'ID: " + breed.getId()));
                    validatedBreeds.add(existingBreed);
                }
            }

            dog.setBreeds(validatedBreeds);
        }

        return dogDao.save(dog);
    }

    @Override
    @Transactional
    public void deleteDog(Long id) {
        rgpdService.anonymizeEntity(Dog.class, id);
    }

    @Override
    public boolean isDogAnonymized(Long id) {
        return rgpdService.isAnonymized(Dog.class, id);
    }

    @Override
    @Transactional
    public Dog updateDog(Long id, Dog dogDetails) {
        Dog existingDog = dogDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chien non trouvé avec l'ID: " + id));

        existingDog.setName(dogDetails.getName());
        existingDog.setBirthDate(dogDetails.getBirthDate());
        existingDog.setGender(dogDetails.getGender());
        existingDog.setChipNumber(dogDetails.getChipNumber());

        if (dogDetails.getOwner() != null && dogDetails.getOwner().getId() != null) {
            Owner owner = ownerDao.findById(dogDetails.getOwner().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Propriétaire non trouvé avec l'ID: " + dogDetails.getOwner().getId()));
            existingDog.setOwner(owner);
        }

        if (dogDetails.getBreeds() != null) {
            if (dogDetails.getBreeds().isEmpty()) {
                throw new IllegalArgumentException("Le chien doit avoir au moins une race");
            }

            if (dogDetails.getBreeds().size() > 3) {
                throw new IllegalArgumentException("Le chien ne peut pas avoir plus de 3 races");
            }

            List<Breed> newBreeds = new ArrayList<>();

            for (Breed breed : dogDetails.getBreeds()) {
                if (breed.getId() != null) {
                    Breed existingBreed = breedDao.findById(breed.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Race non trouvée avec l'ID: " + breed.getId()));
                    newBreeds.add(existingBreed);
                }
                else if (breed.getName() != null && !breed.getName().isEmpty()) {
                    Breed existingBreed = breedDao.findByName(breed.getName())
                            .orElseThrow(() -> new ResourceNotFoundException("Race non trouvée avec le nom: " + breed.getName()));
                    newBreeds.add(existingBreed);
                }
            }

            existingDog.setBreeds(newBreeds);
        }

        return dogDao.save(existingDog);
    }

    @Override
    public long calculateAgeInMonths(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        if (birthDate.isAfter(today)) {
            return 0;
        }
        return Period.between(birthDate, today).toTotalMonths();
    }

    @Override
    public Optional<Dog> getDogByIdAndOwnerId(Long dogId, Long ownerId) {
        // Vérifier d'abord si le chien existe
        Optional<Dog> dogExists = dogDao.findByIdAndNotAnonymized(dogId);

        if (dogExists.isEmpty()) {
            throw new ResourceNotFoundException("Chien non trouvé avec l'ID: " + dogId);
        }

        // Ensuite vérifier la propriété
        Optional<Dog> result = dogDao.findByIdAndOwnerId(dogId, ownerId);

        if (result.isEmpty()) {
            throw new UnauthorizedAccessException("Vous n'avez pas l'autorisation d'accéder à ce chien");
        }

        return result;
    }

    // GARDEZ cette méthode corrigée :
    @Override
    public Dog getDogByOwnerIdAndDogId(Long ownerId, Long dogId) {
        return dogDao.findByIdAndOwnerId(dogId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Dog not found with id: " + dogId + " for owner: " + ownerId));
    }
}