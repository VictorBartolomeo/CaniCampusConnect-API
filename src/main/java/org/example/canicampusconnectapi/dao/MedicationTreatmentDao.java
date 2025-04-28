package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.MedicationTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MedicationTreatmentDao extends JpaRepository<MedicationTreatment, Long> {
    List<MedicationTreatment> findByDog(Dog dog);
    List<MedicationTreatment> findByDogOrderByStartDateDesc(Dog dog);
    List<MedicationTreatment> findByStartDateBetween(Date startDate, Date endDate);
    List<MedicationTreatment> findByDogAndStartDateBetween(Dog dog, Date startDate, Date endDate);
    
    // Find active treatments (where endDate is null or after current date)
    @Query("SELECT mt FROM MedicationTreatment mt WHERE mt.endDate IS NULL OR mt.endDate >= CURRENT_DATE")
    List<MedicationTreatment> findActiveTreatments();
    
    // Find active treatments for a specific dog
    @Query("SELECT mt FROM MedicationTreatment mt WHERE mt.dog = ?1 AND (mt.endDate IS NULL OR mt.endDate >= CURRENT_DATE)")
    List<MedicationTreatment> findActiveTreatmentsByDog(Dog dog);
    
    // Find treatments by name (medication name)
    List<MedicationTreatment> findByNameContaining(String name);
}