package org.example.canicampusconnectapi.service.veterinary;

import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VeterinaryVisitService {

    Optional<VeterinaryVisit> findById(Long id);

    List<VeterinaryVisit> findAll();

    // Renvoie une exception si le chien n'existe pas
    List<VeterinaryVisit> findByDogIdOrderedByDateDesc(Long dogId);

    List<VeterinaryVisit> findByVisitDateBetween(Date startDate, Date endDate);

    // Renvoie une exception si le chien n'existe pas
    List<VeterinaryVisit> findByDogIdAndVisitDateBetween(Long dogId, Date startDate, Date endDate);

    List<VeterinaryVisit> findByReasonContaining(String reason);

    List<VeterinaryVisit> findByVeterinarianContaining(String veterinarian);

    List<VeterinaryVisit> findByDiagnosisContaining(String diagnosis);

    List<VeterinaryVisit> findByTreatmentContaining(String treatment);

    // Renvoie une exception si le chien n'existe pas ou si les données sont invalides
    VeterinaryVisit create(VeterinaryVisit visit);

    // Renvoie Optional.empty si la visite n'existe pas.
    // Renvoie une exception si le chien spécifié (s'il est mis à jour) n'existe pas.
    Optional<VeterinaryVisit> update(Long id, VeterinaryVisit visitDetails);

    boolean deleteById(Long id);
}