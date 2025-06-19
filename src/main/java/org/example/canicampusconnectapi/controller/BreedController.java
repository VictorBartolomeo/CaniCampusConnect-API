package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.security.annotation.role.IsClubOwner;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.breed.BreedService;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@IsClubOwner
public class BreedController {

    private final BreedService breedService; // Utiliser le service au lieu du DAO direct
    private final String uploadDir = "./uploads/breeds/";

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @IsOwner
    @GetMapping("/breed/{id}")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<Breed> getBreed(@PathVariable Short id) {
        return breedService.findById(id)
                .map(breed -> ResponseEntity.ok(breed))
                .orElse(ResponseEntity.notFound().build());
    }

    @IsOwner
    @GetMapping("/breeds")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<List<Breed>> getAllBreeds() {
        List<Breed> breeds = breedService.findAll();
        return ResponseEntity.ok(breeds);
    }

    @PostMapping("/breed")
    public ResponseEntity<Breed> createBreed(@RequestBody @Valid Breed breed) {
        Breed savedBreed = breedService.save(breed);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBreed);
    }

    @PutMapping("/breed/{id}")
    public ResponseEntity<Breed> updateBreed(@PathVariable Short id, @RequestBody @Valid Breed breed) {
        return breedService.findById(id)
                .map(existingBreed -> {
                    breed.setId(id);
                    Breed updatedBreed = breedService.save(breed);
                    return ResponseEntity.ok(updatedBreed);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @IsOwner
    @PostMapping("/breed/{id}/image")
    public ResponseEntity<String> uploadBreedImage(
            @PathVariable Short id,
            @RequestParam("file") MultipartFile file) {

        try {
            Optional<Breed> breedOpt = breedService.findById(id);
            if (breedOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            if (!isValidImageType(file.getContentType())) {
                return ResponseEntity.badRequest().body("Type de fichier non supporté");
            }

            Breed breed = breedOpt.get();
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String fileName = id + "_" +
                    breed.getName().toLowerCase()
                            .replaceAll("\\s+", "-")
                            .replaceAll("[^a-z0-9-]", "") +
                    fileExtension;

            // Créer le répertoire si nécessaire
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/breed/" + id + "/image";
            breedService.updateAvatarUrl(id, imageUrl);

            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload : " + e.getMessage());
        }
    }

    @DeleteMapping("/breed/{id}")
    public ResponseEntity<?> deleteBreed(@PathVariable Short id) {
        Optional<Breed> optionalBreed = breedService.findById(id);
        if (optionalBreed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Breed breed = optionalBreed.get();

        // Vérifier s'il y a des chiens associés à cette race
        if (breed.getDogs() != null && !breed.getDogs().isEmpty()) {
            List<String> dogNames = breed.getDogs().stream()
                    .map(Dog::getName)
                    .collect(Collectors.toList());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Impossible de supprimer cette race car elle est utilisée par " +
                    breed.getDogs().size() + " chien(s). Vous devez d'abord modifier ou supprimer ces chiens.");
            errorResponse.put("affectedDogs", dogNames);
            errorResponse.put("breedId", id);
            errorResponse.put("breedName", breed.getName());
            errorResponse.put("suggestedEndpoint", "/breed/" + id + "/affected-dogs");
            errorResponse.put("timestamp", new Date());
            errorResponse.put("status", 409);
            errorResponse.put("error", "Conflict");
            errorResponse.put("path", "/breed/" + id);

            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        breedService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/breed/{id}/affected-dogs")
    @JsonView(OwnerViewDog.class)
    public ResponseEntity<List<Dog>> getDogsByBreed(@PathVariable Short id) {
        Optional<Breed> optionalBreed = breedService.findById(id);
        if (optionalBreed.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Breed breed = optionalBreed.get();
        List<Dog> dogs = breed.getDogs();

        return ResponseEntity.ok(dogs != null ? dogs : List.of());
    }


    private boolean isValidImageType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/webp")
        );
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }
}