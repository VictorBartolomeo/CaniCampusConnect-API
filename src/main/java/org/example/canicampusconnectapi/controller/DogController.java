package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.common.exception.ResourceNotFound;
import org.example.canicampusconnectapi.common.exception.UnauthorizedAccessException;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.security.AppUserDetails;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.dog.DogService;
import org.example.canicampusconnectapi.view.admin.AdminViewDog;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin
@RestController
public class DogController {

    private final DogService dogService;


    @Autowired
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }



    @IsOwner
    @GetMapping("/dog/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<?> getDog(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            Optional<Dog> optionalDog = dogService.getDogByIdAndOwnerId(id, userDetails.getUserId());
            return new ResponseEntity<>(optionalDog.get(), HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "NOT_FOUND", "message", e.getMessage()));
        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "UNAUTHORIZED_ACCESS", "message", e.getMessage()));
        }
    }

    @IsClubOwner
    @GetMapping("/dogs")
    @JsonView(AdminViewDog.class)
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }

    @IsOwner
    @GetMapping("/owner/{ownerId}/dogs")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<List<Dog>> getDogsByOwner(@PathVariable Long ownerId, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            List<Dog> dogs = dogService.getDogsByOwner(userDetails.getUserId());
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

    @IsOwner
    @DeleteMapping("/dog/{id}")
    public ResponseEntity<Map<String, String>> deleteDog(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            if (dogService.isDogAnonymized(id)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Ce chien est déjà anonymisé",
                        "action", "already_anonymized"
                ));
            }

            dogService.deleteDog(id); // Appelle maintenant le service rgpd

            return ResponseEntity.ok(Map.of(
                    "message", "Données personnelles du chien anonymisées avec succès (conformité RGPD)",
                    "action", "gdpr_anonymized",
                    "dogId", id.toString(),
                    "anonymizedBy", userDetails.getUsername()
            ));
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Chien non trouvé",
                    "action", "error"
            ), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of(
                    "message", "Erreur lors de l'anonymisation GDPR",
                    "action", "error",
                    "details", e.getMessage()
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @IsOwner
    @PutMapping("/dog/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Dog> updateDog(@PathVariable Long id, @RequestBody @Validated(Dog.updateFromOwner.class) Dog dog) {
        try {
            Dog updatedDog = dogService.updateDog(id, dog);
            return new ResponseEntity<>(updatedDog, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
