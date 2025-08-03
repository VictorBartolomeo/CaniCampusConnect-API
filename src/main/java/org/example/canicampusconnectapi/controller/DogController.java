package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.example.canicampusconnectapi.common.exception.ResourceNotFoundException;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.users.Owner;
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
@IsClubOwner
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
            if (userDetails == null || userDetails.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Utilisateur non authentifié"));
            }

            // Vérifier si l'utilisateur est ClubOwner
            boolean isClubOwner = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLUB_OWNER"));

            if (isClubOwner) {
                // ClubOwner peut accéder à n'importe quel chien
                Optional<Dog> optionalDog = dogService.getDogById(id);
                if (optionalDog.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Chien avec l'ID " + id + " introuvable"));
                }
                return ResponseEntity.ok(optionalDog.get());
            } else {
                // Owner ne peut accéder qu'à ses propres chiens
                Dog dog = dogService.getDogByOwnerIdAndDogId(userDetails.getUserId(), id);
                return ResponseEntity.ok(dog);
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Chien introuvable ou accès non autorisé"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur"));
        }
    }

    @GetMapping("/dogs")
    @JsonView(AdminViewDog.class)
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }

    @IsOwner
    @GetMapping("/owner/connected/dogs")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<?> getDogsByOwner(@AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            if (userDetails == null || userDetails.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Utilisateur non authentifié"));
            }

            List<Dog> dogs = dogService.getDogsByOwner(userDetails.getUserId());
            return ResponseEntity.ok(dogs);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Propriétaire introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur"));
        }
    }

    @GetMapping("/owner/{ownerId}/dogs")
    @JsonView(AdminViewDog.class)
    public ResponseEntity<?> getDogsByOwner(@PathVariable Long ownerId) {
        try {
            if (ownerId == null || ownerId <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "ID propriétaire invalide"));
            }

            List<Dog> dogs = dogService.getDogsByOwner(ownerId);
            return ResponseEntity.ok(dogs);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Propriétaire avec l'ID " + ownerId + " introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur"));
        }
    }


    @IsOwner
    @PostMapping("/dog")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<?> createDog(@RequestBody @Validated(Dog.CreateFromOwner.class) Dog dog, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            // Vérifier l'authentification
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Utilisateur non authentifié"));
            }

            // Vérifier si l'utilisateur est ClubOwner (admin)
            boolean isClubOwner = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLUB_OWNER"));

            if (isClubOwner) {
                // ClubOwner DOIT spécifier un owner dans le JSON
                if (dog.getOwner() == null || dog.getOwner().getId() == null) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "En tant qu'administrateur, vous devez spécifier l'ID du propriétaire"));
                }
                // Garder l'owner spécifié dans le JSON
            } else {
                // Owner normal : toujours assigné à lui-même (ignore owner du JSON)
                Owner owner = new Owner();
                owner.setId(userDetails.getUserId());
                dog.setOwner(owner);
            }

            Dog createdDog = dogService.createDog(dog);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDog);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Données invalides : " + e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Race ou propriétaire introuvable"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la création du chien"));
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
        } catch (ResourceNotFoundException e) {
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
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
