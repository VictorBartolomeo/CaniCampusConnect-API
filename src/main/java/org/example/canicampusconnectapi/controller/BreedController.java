package org.example.canicampusconnectapi.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.example.canicampusconnectapi.model.dogRelated.Breed;
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
import java.util.List;
import java.util.Map;
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


    @JsonView(OwnerViewDog.class)
    @GetMapping("/breed/{id}/image")
    public ResponseEntity<Resource> getBreedImage(@PathVariable Short id) {
        try {
            Optional<Breed> breedOpt = breedService.findById(id);
            if (breedOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Breed breed = breedOpt.get();

            // 🗺️ MAPPING EXACT AVEC TES FICHIERS EXISTANTS
            String fileName = mapBreedNameToFileName(breed.getName());

            Path filePath = Paths.get(uploadDir).resolve(fileName);

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "image/png"; // Par défaut PNG car tes fichiers sont en PNG
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


    private String mapBreedNameToFileName(String breedName) {
        Map<String, String> breedToFile = Map.ofEntries(
                Map.entry("Beagle", "Beagle.png"),
                Map.entry("Shih Tzu", "shitzu.png"),
                Map.entry("Teckel", "teckel.png"),
                Map.entry("Maltais", "Maltese.png"),
                Map.entry("Samoyède", "Samoyed.png"),
                Map.entry("Leonberg", "Leonberg.png"),
                Map.entry("Shar Pei", "Shar_Pei.png"),
                Map.entry("Akita Inu", "Akita_Inu.png"),
                Map.entry("Chihuahua", "Chihuahua.png"),
                Map.entry("Chow Chow", "Chow_Chow.png"),
                Map.entry("Dalmatien", "Dalmatien.png"),
                Map.entry("Dobermann", "dobberman.png"),
                Map.entry("Shiba Inu", "shiba_inu.png"),
                Map.entry("Cane Corso", "Cane_Corso.png"),
                Map.entry("Dogue Allemand", "Great_Dane.png"),
                Map.entry("Lhassa Apso", "Lhasa_Apso.png"),
                Map.entry("Rottweiler", "Rottweiler.png"),
                Map.entry("Braque de Weimar", "Waimaraner.png"),
                Map.entry("Basset Hound", "Basset_Hound.png"),
                Map.entry("Bichon Frisé", "Bichon_Frise.png"),
                Map.entry("Setter Irlandais", "Irish_Setter.png"),
                Map.entry("Terre-Neuve", "Newfoundland.png"),
                Map.entry("Border Collie", "Border_Collie.png"),
                Map.entry("Saint-Bernard", "Saint_Bernard.png"),
                Map.entry("Setter Anglais", "English_Setter.png"),
                Map.entry("Bouledogue Français", "French_Bulldog.png"),
                Map.entry("Husky Sibérien", "Siberian_Husky.png"),
                Map.entry("Pointer Anglais", "English_Pointer.png"),
                Map.entry("Berger Allemand", "German_Shepherd.png"),
                Map.entry("Malinois Belge", "Belgian_Malinois.png"),
                Map.entry("Épagneul Breton", "Brittany_Spaniel.png"),
                Map.entry("Golden Retriever", "golden_retriever.png"),
                Map.entry("Yorkshire Terrier", "Yorkshire_Terrier.png"),
                Map.entry("Berger Australien", "australian_sheperd.png"),
                Map.entry("Labrador Retriever", "Labrador_Retriever.png"),
                Map.entry("Bouvier Bernois", "Bernese_Mountain_Dog.png"),
                Map.entry("Jack Russell Terrier", "Jack_Russell_Terrier.png"),
                Map.entry("Corgi du Pays de Galles", "Pembroke_Welsh_Corgi.png"),
                Map.entry("Berger Blanc Suisse", "White_Swiss_Shepherd.png"),
                Map.entry("Cocker Spaniel Anglais", "English_Cocker_Spaniel.png"),
                Map.entry("Braque Allemand à Poil Court", "German_Shorthaired_Pointer.png"),
                Map.entry("Staffordshire Bull Terrier", "Staffordshire_Bull_Terrier.png"),
                Map.entry("West Highland White Terrier", "West_Highland_White_Terrier.png"),
                Map.entry("Cavalier King Charles Spaniel", "Cavalier_King_Charles_Spaniel.png"),
                Map.entry("American Staffordshire Terrier", "American_Staffordshire_Terrier.png")
        );

        return breedToFile.getOrDefault(breedName, "placeholder_no_breed.jpg");
    }

    @IsClubOwner
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