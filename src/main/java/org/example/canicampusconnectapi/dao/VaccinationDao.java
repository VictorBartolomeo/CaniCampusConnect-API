package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.Vaccination;
import org.example.canicampusconnectapi.model.healthRecord.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VaccinationDao extends JpaRepository<Vaccination, Long> {
    List<Vaccination> findByDog(Dog dog);
    List<Vaccination> findByVaccine(Vaccine vaccine);
    List<Vaccination> findByDogAndVaccine(Dog dog, Vaccine vaccine);
    List<Vaccination> findByVaccinationDateBetween(Date startDate, Date endDate);
}