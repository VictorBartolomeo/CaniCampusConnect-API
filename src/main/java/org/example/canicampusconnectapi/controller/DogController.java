package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.EntityNotFoundException;
import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.dto.DogDashboardDTO;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsCoach;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.dog.DogService;
import org.example.canicampusconnectapi.service.dogweight.DogWeightService;
import org.example.canicampusconnectapi.service.veterinary.VeterinaryVisitService;
import org.example.canicampusconnectapi.view.admin.AdminViewDog;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class DogController {

    private final DogService dogService;
    private final VeterinaryVisitService veterinaryVisitService;
    private final DogWeightService dogWeightService;

    @Autowired
    public DogController(DogService dogService,
                         VeterinaryVisitService veterinaryVisitService,
                         DogWeightService dogWeightService) {
        this.dogService = dogService;
        this.veterinaryVisitService = veterinaryVisitService;
        this.dogWeightService = dogWeightService;
    }


    @IsOwner
    @GetMapping("/dog/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> getDog(@PathVariable Long id) {
        Optional<Dog> optionalDog = dogService.getDogById(id);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDog.get(), HttpStatus.OK);
    }

    @IsClubOwner
    @GetMapping("/dogs")
    @JsonView(AdminViewDog.class)
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }

    @IsCoach
    @GetMapping("/dogs/name/{name}")
    @JsonView(CoachView.class)
    public List<Dog> getDogsByName(@PathVariable String name) {
        return dogService.getDogsByName(name);
    }

    @IsCoach
    @GetMapping("/dog/chip/{chipNumber}")
    @JsonView(CoachView.class)
    public ResponseEntity<Dog> getDogByChipNumber(@PathVariable String chipNumber) {
        Optional<Dog> optionalDog = dogService.getDogByChipNumber(chipNumber);
        if (optionalDog.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalDog.get(), HttpStatus.OK);
    }

    @IsOwner
    @GetMapping("/owner/{ownerId}/dogs")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<List<Dog>> getDogsByOwner(@PathVariable Long ownerId) {
        try {
            List<Dog> dogs = dogService.getDogsByOwner(ownerId);
            return new ResponseEntity<>(dogs, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @IsOwner
    @PostMapping("/dog")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        try {
            Dog createdDog = dogService.createDog(dog);
            return new ResponseEntity<>(createdDog, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @IsClubOwner
    @DeleteMapping("/dog/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable Long id) {
        try {
            dogService.deleteDog(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @IsOwner
    @PutMapping("/dog/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @RequestBody Dog dog) {
        try {
            Dog updatedDog = dogService.updateDog(id, dog);
            return new ResponseEntity<>(updatedDog, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/dogs/{dogId}/dashboard")
//    public ResponseEntity<DogDashboardDTO> getDogDashboard(@PathVariable Long dogId) {
//        try {
//            Dog dogInfo = dogService.findById(dogId) // Assurez-vous que findById retourne Dog ou Optional<Dog>
//                    .orElseThrow(() -> new EntityNotFoundException("Dog not found with id: " + dogId));
//
//            // Récupérer les 5 visites les plus récentes, par exemple
//            // Vous devrez peut-être ajouter une méthode à votre service/repository pour cela
//            // Ex: findByDogIdOrderByVisitDateDesc avec une limite
//            List<VeterinaryVisit> recentVisits = veterinaryVisitService.findTopNByDogIdOrderByDateDesc(dogId, 5);
//
//            // Récupérer les 5 poids les plus récents, par exemple
//            // Ex: findByDogIdOrderByDateDesc avec une limite
//            List<DogWeight> recentWeights = dogWeightService.findTopNByDogIdOrderByDateDesc(dogId, 5);
//
//
//            DogDashboardDTO dashboardData = new DogDashboardDTO();
//            dashboardData.setDogInfo(dogInfo);
//            dashboardData.setRecentVeterinaryVisits(recentVisits);
//            dashboardData.setRecentWeights(recentWeights);
//            // Remplissez d'autres données si nécessaire
//
//            return ResponseEntity.ok(dashboardData);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
//        }
//        // Gérez d'autres exceptions potentielles (ex: erreurs de service)
//    }

}
