package org.example.canicampusconnectapi.service;

import jakarta.persistence.EntityNotFoundException; // Utilisation d'une exception standard
import org.example.canicampusconnectapi.dao.DogDao;
import org.example.canicampusconnectapi.dao.VeterinaryVisitDao;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VeterinaryVisitServiceImpl implements VeterinaryVisitService {

    private final VeterinaryVisitDao veterinaryVisitDao;
    private final DogDao dogDao;

    @Autowired
    public VeterinaryVisitServiceImpl(VeterinaryVisitDao veterinaryVisitDao, DogDao dogDao) {
        this.veterinaryVisitDao = veterinaryVisitDao;
        this.dogDao = dogDao;
    }

    private Dog findDogByIdOrThrow(Long dogId) {
        return dogDao.findById(dogId)
                .orElseThrow(() -> new EntityNotFoundException("Dog not found with id: " + dogId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VeterinaryVisit> findById(Long id) {
        return veterinaryVisitDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findAll() {
        return veterinaryVisitDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findByDogIdOrderedByDateDesc(Long dogId) {
        Dog dog = findDogByIdOrThrow(dogId);
        return veterinaryVisitDao.findByDogOrderByVisitDateDesc(dog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findByVisitDateBetween(Date startDate, Date endDate) {
        return veterinaryVisitDao.findByVisitDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findByDogIdAndVisitDateBetween(Long dogId, Date startDate, Date endDate) {
        Dog dog = findDogByIdOrThrow(dogId);
        return veterinaryVisitDao.findByDogAndVisitDateBetween(dog, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findByReasonContaining(String reason) {
        return veterinaryVisitDao.findByReasonForVisitContaining(reason);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findByVeterinarianContaining(String veterinarian) {
        return veterinaryVisitDao.findByVeterinarianContaining(veterinarian);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findByDiagnosisContaining(String diagnosis) {
        return veterinaryVisitDao.findByDiagnosisContaining(diagnosis);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VeterinaryVisit> findByTreatmentContaining(String treatment) {
        return veterinaryVisitDao.findByTreatmentContaining(treatment);
    }

    @Override
    @Transactional
    public VeterinaryVisit create(VeterinaryVisit visit) {
        if (visit.getDog() == null || visit.getDog().getId() == null) {
            throw new IllegalArgumentException("Dog ID cannot be null for veterinary visit");
        }
        // Vérifie que le chien existe et récupère l'entité managée
        Dog dog = findDogByIdOrThrow(visit.getDog().getId());
        visit.setDog(dog); // Assigne l'entité managée
        // Ajout de validations potentielles sur les champs requis (ex: visitDate)
        if (visit.getVisitDate() == null) {
            throw new IllegalArgumentException("Visit date cannot be null");
        }

        return veterinaryVisitDao.save(visit);
    }

    @Override
    @Transactional
    public Optional<VeterinaryVisit> update(Long id, VeterinaryVisit visitDetails) {
        return veterinaryVisitDao.findById(id)
                .map(existingVisit -> {
                    // Mettre à jour les champs autorisés
                    if (visitDetails.getVisitDate() != null) {
                        existingVisit.setVisitDate(visitDetails.getVisitDate());
                    }
                    if (visitDetails.getDiagnosis() != null) {
                        existingVisit.setDiagnosis(visitDetails.getDiagnosis());
                    }
                    if (visitDetails.getReasonForVisit() != null) {
                        existingVisit.setReasonForVisit(visitDetails.getReasonForVisit());
                    }
                    if (visitDetails.getTreatment() != null) {
                        existingVisit.setTreatment(visitDetails.getTreatment());
                    }
                    if (visitDetails.getVeterinarian() != null) {
                        existingVisit.setVeterinarian(visitDetails.getVeterinarian());
                    }

                    // Gérer la mise à jour potentielle du chien associé
                    if (visitDetails.getDog() != null && visitDetails.getDog().getId() != null &&
                            !visitDetails.getDog().getId().equals(existingVisit.getDog().getId())) {
                        // Si un nouveau Dog ID est fourni et différent de l'actuel, le valider et le mettre à jour
                        Dog newDog = findDogByIdOrThrow(visitDetails.getDog().getId());
                        existingVisit.setDog(newDog);
                    }
                    // Note : Si visitDetails.getDog() est null ou a un ID null, on ne touche pas au chien existant.

                    return veterinaryVisitDao.save(existingVisit);
                }); // Si findById retourne Optional.empty, map ne s'exécute pas et on retourne Optional.empty
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (veterinaryVisitDao.existsById(id)) {
            veterinaryVisitDao.deleteById(id);
            return true;
        }
        return false;
    }
}