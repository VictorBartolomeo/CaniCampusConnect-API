package org.example.canicampusconnectapi.controller;

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
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/images")
public class ImageController {

    private final String uploadDir = "./uploads/breeds/";

    @PostMapping("/breed/{breedId}")
    public ResponseEntity<String> uploadBreedImage(
            @PathVariable Short breedId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Vérifications du fichier
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            String fileName = breedId + "_" + UUID.randomUUID() +
                    getFileExtension(file.getOriginalFilename());

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Mettre à jour la base de données
            String imageUrl = "/api/images/breeds/" + fileName;
            breedService.updateAvatarUrl(breedId, imageUrl);

            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'upload");
        }
    }

    @GetMapping("/breeds/{filename}")
    public ResponseEntity<Resource> getBreedImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // ou détection auto
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}