package org.example.canicampusconnectapi.dao;

import org.example.canicampusconnectapi.model.Dog;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VeterinaryVisitDao extends JpaRepository<VeterinaryVisit, Long> {
    List<VeterinaryVisit> findByDog(Dog dog);
    List<VeterinaryVisit> findByDogOrderByVisitDateDesc(Dog dog);
    List<VeterinaryVisit> findByVisitDateBetween(Date startDate, Date endDate);
    List<VeterinaryVisit> findByDogAndVisitDateBetween(Dog dog, Date startDate, Date endDate);
    List<VeterinaryVisit> findByReasonForVisitContaining(String reason);
    List<VeterinaryVisit> findByVeterinarianContaining(String veterinarian);
    List<VeterinaryVisit> findByDiagnosisContaining(String diagnosis);
    List<VeterinaryVisit> findByTreatmentContaining(String treatment);
}