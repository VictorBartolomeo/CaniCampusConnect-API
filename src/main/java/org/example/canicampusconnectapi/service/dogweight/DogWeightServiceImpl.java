package org.example.canicampusconnectapi.service.dogweight;

import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.DogWeightDao;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DogWeightServiceImpl implements DogWeightService {

    private final DogWeightDao dogWeightDao;
    private final DogDao dogDao;

    @Autowired
    public DogWeightServiceImpl(DogWeightDao dogWeightDao, DogDao dogDao) {
        this.dogWeightDao = dogWeightDao;
        this.dogDao = dogDao;
    }

    @Override
    public Optional<DogWeight> getDogWeightById(Long id) {
        return dogWeightDao.findById(id);
    }

    @Override
    public List<DogWeight> getAllDogWeights() {
        return dogWeightDao.findAll();
    }

    @Override
    public List<DogWeight> getDogWeightsByDog(Long dogId) {
        Dog dog = dogDao.findById(dogId)
                .orElseThrow(() -> new ResourceNotFound("Dog not found with id: " + dogId));
        return dogWeightDao.findByDogOrderByMeasurementDateDesc(dog);
    }

    @Override
    public List<DogWeight> getDogWeightsBetweenDates(Date startDate, Date endDate) {
        return dogWeightDao.findByMeasurementDateBetween(startDate, endDate);
    }

    @Override
    public List<DogWeight> getDogWeightsByDogBetweenDates(Long dogId, Date startDate, Date endDate) {
        Dog dog = dogDao.findById(dogId)
                .orElseThrow(() -> new ResourceNotFound("Dog not found with id: " + dogId));
        return dogWeightDao.findByDogAndMeasurementDateBetween(dog, startDate, endDate);
    }

    @Override
    @Transactional
    public DogWeight createDogWeight(DogWeight dogWeight) {
        if (dogWeight.getDog() == null || dogWeight.getDog().getId() == null) {
            throw new IllegalArgumentException("Dog ID must be provided to create a dog weight record.");
        }

        Dog dog = dogDao.findById(dogWeight.getDog().getId())
                .orElseThrow(() -> new ResourceNotFound("Dog not found with id: " + dogWeight.getDog().getId()));

        // Ensure the full dog object is set before saving
        dogWeight.setDog(dog);
        // Clear ID to ensure creation, not update
        dogWeight.setId(null);
        return dogWeightDao.save(dogWeight);
    }

    @Override
    @Transactional
    public void deleteDogWeight(Long id) {
        if (!dogWeightDao.existsById(id)) {
            throw new ResourceNotFound("Dog weight record not found with id: " + id);
        }
        dogWeightDao.deleteById(id);
    }

    @Override
    @Transactional
    public DogWeight updateDogWeight(Long id, DogWeight dogWeightDetails) {
        DogWeight existingDogWeight = dogWeightDao.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Dog weight record not found with id: " + id));

        // Update basic fields
        existingDogWeight.setWeightValue(dogWeightDetails.getWeightValue());
        existingDogWeight.setMeasurementDate(dogWeightDetails.getMeasurementDate());
        existingDogWeight.setUnit(dogWeightDetails.getUnit());
        // Add other fields from DogWeight entity that should be updatable

        // Handle dog update only if provided in the request
        if (dogWeightDetails.getDog() != null && dogWeightDetails.getDog().getId() != null) {
            // Check if the dog is changing
            if (!dogWeightDetails.getDog().getId().equals(existingDogWeight.getDog().getId())) {
                Dog newDog = dogDao.findById(dogWeightDetails.getDog().getId())
                        .orElseThrow(() -> new ResourceNotFound("Dog not found with id: " + dogWeightDetails.getDog().getId()));
                existingDogWeight.setDog(newDog);
            }
            // If the ID is the same, no need to fetch or update the dog relationship
        }
        // If dog details are null in dogWeightDetails, we keep the existing dog.

        return dogWeightDao.save(existingDogWeight);
    }

    @Override
    public List<DogWeight> getDogWeightsFromLast7Days(Long dogId) {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();

        return getDogWeightsByDogBetweenDates(dogId, startDate, endDate);
    }

    @Override
    public List<DogWeight> getDogWeightsFromLast3Months(Long dogId) {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();

        calendar.add(Calendar.MONTH, -3);
        Date startDate = calendar.getTime();

        return getDogWeightsByDogBetweenDates(dogId, startDate, endDate);
    }

    @Override
    public List<DogWeight> getDogWeightsFromLast6Months(Long dogId) {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();

        calendar.add(Calendar.MONTH, -6);
        Date startDate = calendar.getTime();

        return getDogWeightsByDogBetweenDates(dogId, startDate, endDate);
    }

    @Override
    public List<DogWeight> getDogWeightsFromLast12Months(Long dogId) {
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();

        calendar.add(Calendar.MONTH, -12);
        Date startDate = calendar.getTime();

        return getDogWeightsByDogBetweenDates(dogId, startDate, endDate);
    }
}
