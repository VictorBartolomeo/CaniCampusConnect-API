package org.example.canicampusconnectapi.service.dogweight;

import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.model.dogRelated.Dog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing dog weight records.
 */
public interface DogWeightService {

    /**
     * Finds a dog weight record by its ID.
     *
     * @param id The ID of the dog weight record.
     * @return An Optional containing the dog weight record if found, otherwise empty.
     */
    Optional<DogWeight> getDogWeightById(Long id);

    /**
     * Retrieves all dog weight records.
     *
     * @return A list of all dog weight records.
     */
    List<DogWeight> getAllDogWeights();

    /**
     * Retrieves all weight records for a specific dog.
     *
     * @param dogId The ID of the dog.
     * @return A list of weight records for the dog, ordered by measurement date (descending).
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog is not found.
     */
    List<DogWeight> getDogWeightsByDog(Long dogId);

    /**
     * Retrieves all dog weight records between two dates.
     *
     * @param startDate The start date.
     * @param endDate The end date.
     * @return A list of dog weight records between the specified dates.
     */
    List<DogWeight> getDogWeightsBetweenDates(Date startDate, Date endDate);

    /**
     * Retrieves all weight records for a specific dog between two dates.
     *
     * @param dogId The ID of the dog.
     * @param startDate The start date.
     * @param endDate The end date.
     * @return A list of weight records for the dog between the specified dates.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog is not found.
     */
    List<DogWeight> getDogWeightsByDogBetweenDates(Long dogId, Date startDate, Date endDate);

    /**
     * Creates a new dog weight record. Validates dog existence.
     *
     * @param dogWeight The dog weight object to create. Must have a valid dog ID set.
     * @return The created dog weight record with its generated ID.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the specified dog does not exist.
     * @throws IllegalArgumentException if the dog information is missing.
     */
    DogWeight createDogWeight(DogWeight dogWeight);

    /**
     * Deletes a dog weight record by its ID.
     *
     * @param id The ID of the dog weight record to delete.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog weight record with the given ID does not exist.
     */
    void deleteDogWeight(Long id);

    /**
     * Updates an existing dog weight record.
     *
     * @param id The ID of the dog weight record to update.
     * @param dogWeight The dog weight data to update. Dog can be updated if provided.
     * @return The updated dog weight record.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog weight record or the specified new dog does not exist.
     */
    DogWeight updateDogWeight(Long id, DogWeight dogWeight);

    /**
     * Retrieves all weight records for a specific dog from the last 7 days.
     *
     * @param dogId The ID of the dog.
     * @return A list of weight records for the dog from the last 7 days.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog is not found.
     */
    List<DogWeight> getDogWeightsFromLast7Days(Long dogId);

    /**
     * Retrieves all weight records for a specific dog from the last 3 months.
     *
     * @param dogId The ID of the dog.
     * @return A list of weight records for the dog from the last 3 months.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog is not found.
     */
    List<DogWeight> getDogWeightsFromLast3Months(Long dogId);

    /**
     * Retrieves all weight records for a specific dog from the last 6 months.
     *
     * @param dogId The ID of the dog.
     * @return A list of weight records for the dog from the last 6 months.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog is not found.
     */
    List<DogWeight> getDogWeightsFromLast6Months(Long dogId);

    /**
     * Retrieves all weight records for a specific dog from the last 12 months.
     *
     * @param dogId The ID of the dog.
     * @return A list of weight records for the dog from the last 12 months.
     * @throws org.example.canicampusconnectapi.common.exception.ResourceNotFound if the dog is not found.
     */
    List<DogWeight> getDogWeightsFromLast12Months(Long dogId);
}
