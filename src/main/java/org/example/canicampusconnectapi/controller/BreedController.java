package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.example.canicampusconnectapi.security.annotation.role.IsOwner;
import org.example.canicampusconnectapi.service.breed.BreedService;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class BreedController {

    private final BreedService breedService; // Utiliser le service au lieu du DAO direct
    private final String uploadDir = "./uploads/breeds/";

    public BreedController(BreedService breedService) {
        this.breedService = breedService;
    }

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

    @DeleteMapping("/breed/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable Short id) {
        if (breedService.existsById(id)) {
            breedService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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

    // 📸 GESTION DES IMAGES
    @PostMapping("/breed/{id}/image")
    public ResponseEntity<String> uploadBreedImage(
            @PathVariable Short id,
            @RequestParam("file") MultipartFile file) {

        try {
            // Vérifier que la race existe
            Optional<Breed> breedOpt = breedService.findById(id);
            if (breedOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Validations
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            if (!isValidImageType(file.getContentType())) {
                return ResponseEntity.badRequest().body("Type de fichier non supporté");
            }

            // Générer le nom de fichier
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

            // Sauvegarder le fichier
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 🔗 LIEN EN BDD : Mettre à jour l'avatarUrl
            String imageUrl = "/api/breed/" + id + "/image";
            breedService.updateAvatarUrl(id, imageUrl);

            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload : " + e.getMessage());
        }
    }

    @GetMapping("/breed/{id}/image")
    public ResponseEntity<Resource> getBreedImage(@PathVariable Short id) {
        try {
            // Récupérer la race pour construire le nom de fichier
            Optional<Breed> breedOpt = breedService.findById(id);
            if (breedOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Breed breed = breedOpt.get();

            // Chercher le fichier (essayer plusieurs extensions)
            String baseName = id + "_" + breed.getName().toLowerCase()
                    .replaceAll("\\s+", "-")
                    .replaceAll("[^a-z0-9-]", "");

            String[] extensions = {".jpg", ".jpeg", ".png", ".webp"};
            Path filePath = null;

            for (String ext : extensions) {
                Path testPath = Paths.get(uploadDir).resolve(baseName + ext);
                if (Files.exists(testPath)) {
                    filePath = testPath;
                    break;
                }
            }

            if (filePath == null || !Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "image/jpeg";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/breed/{id}/image")
    public ResponseEntity<Void> deleteBreedImage(@PathVariable Short id) {
        try {
            Optional<Breed> breedOpt = breedService.findById(id);
            if (breedOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Supprimer le fichier physique
            Breed breed = breedOpt.get();
            String baseName = id + "_" + breed.getName().toLowerCase()
                    .replaceAll("\\s+", "-")
                    .replaceAll("[^a-z0-9-]", "");

            String[] extensions = {".jpg", ".jpeg", ".png", ".webp"};
            boolean fileDeleted = false;

            for (String ext : extensions) {
                Path testPath = Paths.get(uploadDir).resolve(baseName + ext);
                if (Files.exists(testPath)) {
                    Files.delete(testPath);
                    fileDeleted = true;
                    break;
                }
            }

            // Mettre à jour en BDD (supprimer l'URL)
            breedService.updateAvatarUrl(id, null);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Méthodes utilitaires
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