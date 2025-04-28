package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface DogWeightDao extends JpaRepository<DogWeight, Long> {
    List<DogWeight> findByDog(Dog dog);
    List<DogWeight> findByDogOrderByMeasurementDateDesc(Dog dog);
    List<DogWeight> findByMeasurementDateBetween(Date startDate, Date endDate);
    List<DogWeight> findByDogAndMeasurementDateBetween(Dog dog, Date startDate, Date endDate);
}