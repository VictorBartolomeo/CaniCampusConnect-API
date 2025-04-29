package org.example.canicampusconnectapi.service.dog;

import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.OwnerDao;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class DogServiceImpl implements DogService {

    private final DogDao dogDao;
    private final OwnerDao ownerDao;

    @Autowired // Constructor injection
    public DogServiceImpl(DogDao dogDao, OwnerDao ownerDao) {
        this.dogDao = dogDao;
        this.ownerDao = ownerDao;
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
    public List<Dog> getDogsByName(String name) {
        return dogDao.findByName(name);
    }

    @Override
    public Optional<Dog> getDogByChipNumber(String chipNumber) {
        return dogDao.findByChipNumber(chipNumber);
    }

    @Override
    public List<Dog> getDogsByOwner(Long ownerId) {
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFound("Owner not found with id: " + ownerId));
        return dogDao.findByOwner(owner);
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
        if (!dogDao.existsById(id)) {
            throw new ResourceNotFound("Dog not found with id: " + id);
        }
        dogDao.deleteById(id);
    }

    @Override
    @Transactional
    public Dog updateDog(Long id, Dog dogDetails) {
        Dog existingDog = dogDao.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Dog not found with id: " + id));

        // Update basic fields
        existingDog.setName(dogDetails.getName());
        existingDog.setBirthDate(dogDetails.getBirthDate());
        existingDog.setGender(dogDetails.getGender());
        existingDog.setChipNumber(dogDetails.getChipNumber());
        // Add other fields from Dog entity that should be updatable

        // Handle owner update only if provided in the request
        if (dogDetails.getOwner() != null && dogDetails.getOwner().getId() != null) {
            // Check if the owner is changing
            if (!dogDetails.getOwner().getId().equals(existingDog.getOwner().getId())) {
                Owner newOwner = ownerDao.findById(dogDetails.getOwner().getId())
                        .orElseThrow(() -> new ResourceNotFound("Owner not found with id: " + dogDetails.getOwner().getId()));
                existingDog.setOwner(newOwner);
            }
            // If the ID is the same, no need to fetch or update the owner relationship
        }
        // If owner details are null in dogDetails, we keep the existing owner.

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