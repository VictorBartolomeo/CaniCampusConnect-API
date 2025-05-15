package org.example.canicampusconnectapi.service.dog;

import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DogService {

    /**
     * Finds a dog by its ID.
     *
     * @param id The ID of the dog.
     * @return An Optional containing the dog if found, otherwise empty.
     */
    Optional<Dog> getDogById(Long id);

    /**
     * Retrieves all dogs.
     *
     * @return A list of all dogs.
     */
    List<Dog> getAllDogs();

    /**
     * Retrieves all dogs belonging to a specific owner.
     *
     * @param ownerId The ID of the owner.
     * @return A list of dogs belonging to the owner.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the owner is not found.
     */
    List<Dog> getDogsByOwner(Long ownerId);

    /**
     * Creates a new dog. Validates owner existence.
     *
     * @param dog The dog object to create. Must have a valid owner ID set.
     * @return The created dog with its generated ID.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the specified owner does not exist.
     * @throws IllegalArgumentException if the owner information is missing.
     */
    Dog createDog(Dog dog);

    /**
     * Deletes a dog by its ID.
     *
     * @param id The ID of the dog to delete.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog with the given ID does not exist.
     */
    void deleteDog(Long id);

    /**
     * Updates an existing dog.
     *
     * @param id The ID of the dog to update.
     * @param dog The dog data to update. Owner can be updated if provided.
     * @return The updated dog.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog or the specified new owner does not exist.
     */
    Dog updateDog(Long id, Dog dog);

    /**
     * Calculates the age of a dog in months based on its birth date.
     *
     * @param birthDate The birth date of the dog.
     * @return The age of the dog in total months. Returns 0 if birthDate is null or in the future.
     */
    long calculateAgeInMonths(LocalDate birthDate);

    Dog getDogByOwnerIdAndDogId (Long ownerId, Long dogId);

}