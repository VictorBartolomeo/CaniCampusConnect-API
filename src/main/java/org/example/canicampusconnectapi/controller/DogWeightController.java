package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.dog.DogService;
import org.example.canicampusconnectapi.service.dogweight.DogWeightService;
import org.example.canicampusconnectapi.view.utilities.WeightView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@IsClubOwner
public class DogWeightController {

    private final DogWeightService dogWeightService;
    protected DogService dogService;

    @Autowired
    public DogWeightController(DogWeightService dogWeightService, DogService dogService) {
        this.dogWeightService = dogWeightService;
        this.dogService = dogService;
    }

    @GetMapping("/dogweights")
    public List<DogWeight> getAllDogWeights() {
        return dogWeightService.getAllDogWeights();
    }

    @IsOwner
    @JsonView(WeightView.class)
    @GetMapping("/dog/{dogId}/weights")
    public ResponseEntity<?> getDogWeightsByDog(@PathVariable Long dogId, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            // Vérifier l'authentification
            if (userDetails == null || userDetails.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Utilisateur non authentifié"));
            }

            // Vérifier si l'utilisateur est ClubOwner
            boolean isClubOwner = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLUB_OWNER"));

            if (isClubOwner) {
                // ClubOwner peut accéder aux poids de n'importe quel chien
                List<DogWeight> dogWeights = dogWeightService.getDogWeightsByDog(dogId);
                return ResponseEntity.ok(dogWeights);
            } else {
                // Owner : vérifier que le chien lui appartient
                // D'abord vérifier si le chien existe et appartient à l'owner
                Optional<Dog> dogOptional = dogService.getDogByIdAndOwnerId(dogId, userDetails.getUserId());
                if (dogOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Accès refusé : ce chien ne vous appartient pas"));
                }

                // Si le chien appartient à l'owner, récupérer ses poids
                List<DogWeight> dogWeights = dogWeightService.getDogWeightsByDog(dogId);
                return ResponseEntity.ok(dogWeights);
            }

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Chien avec l'ID " + dogId + " introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur"));
        }
    }


    @IsOwner
    @PostMapping("/dog/{dogId}/weight")
    @JsonView(WeightView.class)
    public ResponseEntity<?> createDogWeight(@PathVariable Long dogId, @RequestBody DogWeight dogWeight, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            // Vérifier l'authentification
            if (userDetails == null || userDetails.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Utilisateur non authentifié"));
            }

            // Vérifier si l'utilisateur est ClubOwner
            boolean isClubOwner = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLUB_OWNER"));

            if (!isClubOwner) {
                // Owner : vérifier que le chien lui appartient
                Optional<Dog> dogOptional = dogService.getDogByIdAndOwnerId(dogId, userDetails.getUserId());
                if (dogOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of("error", "Accès refusé : ce chien ne vous appartient pas"));
                }
            }

            // Récupérer le chien et l'assigner au DogWeight
            Optional<Dog> dogOptional = dogService.getDogById(dogId);
            if (dogOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Chien avec l'ID " + dogId + " introuvable"));
            }

            // Assigner automatiquement le chien depuis l'URL
            dogWeight.setDog(dogOptional.get());

            DogWeight createdDogWeight = dogWeightService.createDogWeight(dogWeight);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDogWeight);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Données invalides : " + e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Chien introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la création du poids"));
        }
    }

    @DeleteMapping("dog/{dogId}/weight/{id}")
    public ResponseEntity<DogWeight> deleteDogWeight(@PathVariable Long id) {
        try {
            dogWeightService.deleteDogWeight(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/dogweight/{id}")
    public ResponseEntity<DogWeight> updateDogWeight(@PathVariable Long id, @RequestBody DogWeight dogWeight) {
        try {
            DogWeight updatedDogWeight = dogWeightService.updateDogWeight(id, dogWeight);
            return new ResponseEntity<>(updatedDogWeight, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
