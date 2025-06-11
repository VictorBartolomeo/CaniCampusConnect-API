package org.example.canicampusconnectapi.service.dog;

import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.service.rgpd.RgpdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.canicampusconnectapi.dao.BreedDao;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                .orElseThrow(() -> new ResourceNotFound("Owner not found with id: " + ownerId));
        return dogDao.findByOwner(owner);
    }

    @Override
    public Dog getDogByOwnerIdAndDogId(Long ownerId, Long dogId) {
        Dog dog = dogDao.findById(dogId)
                .orElseThrow(() -> new ResourceNotFound("Owner not found with id: " + ownerId));
        return dogDao.findUniqueByOwner(dog);
    }

    @Override
    @Transactional // Ensure atomicity
    public Dog createDog(Dog dog) {
        if (dog.getOwner() == null || dog.getOwner().getId() == null) {
            throw new IllegalArgumentException("Owner ID must be provided to create a dog.");
        }

        Owner owner = ownerDao.findById(dog.getOwner().getId())
                .orElseThrow(() -> new ResourceNotFound("Owner not found with id: " + dog.getOwner().getId()));

        // Ensure the full owner object is set before saving
        dog.setOwner(owner);
        // Clear ID to ensure creation, not update
        dog.setId(null);
        return dogDao.save(dog);
    }

    @Override
    @Transactional
    public void deleteDog(Long id) {
        // L'appel à findById est implicite dans le service d'anonymisation
        // qui lancera une exception si le chien n'est pas trouvé.
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
                .orElseThrow(() -> new ResourceNotFound("Chien non trouvé avec l'ID: " + id));

        existingDog.setName(dogDetails.getName());
        existingDog.setBirthDate(dogDetails.getBirthDate());
        existingDog.setGender(dogDetails.getGender());
        existingDog.setChipNumber(dogDetails.getChipNumber());

        if (dogDetails.getOwner() != null && dogDetails.getOwner().getId() != null) {
            Owner owner = ownerDao.findById(dogDetails.getOwner().getId())
                    .orElseThrow(() -> new ResourceNotFound("Propriétaire non trouvé avec l'ID: " + dogDetails.getOwner().getId()));
            existingDog.setOwner(owner);
        }

        if (dogDetails.getBreeds() != null) {
            if (dogDetails.getBreeds().isEmpty()) {
                throw new IllegalArgumentException("Le chien doit avoir au moins une race");
            }

            if (dogDetails.getBreeds().size() > 3) {
                throw new IllegalArgumentException("Le chien ne peut pas avoir plus de 3 races");
            }
            Set<Breed> newBreeds = new HashSet<>();

            for (Breed breed : dogDetails.getBreeds()) {
                if (breed.getId() != null) {
                    Breed existingBreed = breedDao.findById(breed.getId())
                            .orElseThrow(() -> new ResourceNotFound("Race non trouvée avec l'ID: " + breed.getId()));
                    newBreeds.add(existingBreed);
                }
                // Sinon, si la race a un nom, essayer de la trouver par son nom
                else if (breed.getName() != null && !breed.getName().isEmpty()) {
                    Breed existingBreed = breedDao.findByName(breed.getName())
                            .orElseThrow(() -> new ResourceNotFound("Race non trouvée avec le nom: " + breed.getName()));
                    newBreeds.add(existingBreed);
                }
            }

            // Remplacer l'ancien ensemble de races par le nouveau
            existingDog.setBreeds(newBreeds);
        }

        // Sauvegarder et retourner le chien mis à jour
        return dogDao.save(existingDog);
    }


    @Override
    public long calculateAgeInMonths(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        if (birthDate.isAfter(today)) {
            return 0; // Birth date in the future
        }
        // Calculate the period and return total months
        return Period.between(birthDate, today).toTotalMonths();
    }

}