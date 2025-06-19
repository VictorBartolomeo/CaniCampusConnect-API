package org.example.canicampusconnectapi.controller;

import org.example.canicampusconnectapi.model.dogRelated.Breed;
import org.example.canicampusconnectapi.service.breed.BreedService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
public class PublicBreedController {

    private final BreedService breedService;
    private final String uploadDir = "./uploads/breeds/";

    public PublicBreedController(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping("/breed/{id}/image")
    public ResponseEntity<Resource> getBreedImage(@PathVariable Short id) {
        try {
            Optional<Breed> breedOpt = breedService.findById(id);
            if (breedOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Breed breed = breedOpt.get();
            String fileName = mapBreedNameToFileName(breed.getName());
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "image/png";
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
                Map.entry("Berger Australien", "australian_sheperd.png"),
                Map.entry("Golden Retriever", "golden_retriever.png"),
                Map.entry("Staffordshire Bull Terrier", "Staffordshire_Bull_Terrier.png"),
                Map.entry("Berger Belge", "Belgian_Malinois.png"),
                Map.entry("Labrador Retriever", "Labrador_Retriever.png"),
                Map.entry("Berger Allemand", "German_Shepherd.png"),
                Map.entry("Setter Anglais", "English_Setter.png"),
                Map.entry("Épagneul Breton", "Brittany_Spaniel.png"),
                Map.entry("Beagle", "Beagle.png"),
                Map.entry("Cavalier King Charles Spaniel", "Cavalier_King_Charles_Spaniel.png"),
                Map.entry("Bouledogue Français", "French_Bulldog.png"),
                Map.entry("Cocker Spaniel Anglais", "English_Cocker_Spaniel.png"),
                Map.entry("Teckel", "teckel.png"),
                Map.entry("Yorkshire Terrier", "Yorkshire_Terrier.png"),
                Map.entry("Cane Corso", "Cane_Corso.png"),
                Map.entry("Husky de Sibérie", "Siberian_Husky.png"),
                Map.entry("Shih Tzu", "shitzu.png"),
                Map.entry("American Staffordshire Terrier", "American_Staffordshire_Terrier.png"),
                Map.entry("Chihuahua", "Chihuahua.png"),
                Map.entry("Jack Russell Terrier", "Jack_Russell_Terrier.png"),
                Map.entry("Border Collie", "Border_Collie.png"),
                Map.entry("Pointer Anglais", "English_Pointer.png"),
                Map.entry("Dogue Allemand", "Great_Dane.png"),
                Map.entry("Rottweiler", "Rottweiler.png"),
                Map.entry("Berger Blanc Suisse", "White_Swiss_Shepherd.png"),
                Map.entry("Samoyède", "Samoyed.png"),
                Map.entry("West Highland White Terrier", "West_Highland_White_Terrier.png"),
                Map.entry("Basset Hound", "Basset_Hound.png"),
                Map.entry("Dalmatien", "Dalmatien.png"),
                Map.entry("Akita Inu", "Akita_Inu.png"),
                Map.entry("Shiba Inu", "shiba_inu.png"),
                Map.entry("Bichon Frisé", "Bichon_Frise.png"),
                Map.entry("Bichon Maltais", "Maltese.png"),
                Map.entry("Lhassa Apso", "Lhasa_Apso.png"),
                Map.entry("Shar Pei", "Shar_Pei.png"),
                Map.entry("Chow Chow", "Chow_Chow.png"),
                Map.entry("Dobermann", "dobberman.png"),
                Map.entry("Leonberg", "Leonberg.png"),
                Map.entry("Terre-Neuve", "Newfoundland.png"),
                Map.entry("Bouvier Bernois", "Bernese_Mountain_Dog.png"),
                Map.entry("Saint-Bernard", "Saint_Bernard.png"),
                Map.entry("Welsh Corgi Pembroke", "Pembroke_Welsh_Corgi.png"),
                Map.entry("Setter Irlandais Rouge", "Irish_Setter.png"),
                Map.entry("Braque Allemand", "German_Shorthaired_Pointer.png"),
                Map.entry("Braque de Weimar", "Waimaraner.png"),
                Map.entry("Griffon Korthals", "Wirehaired_Pointing_Griff.png"), // Pas trouvé
                Map.entry("Braque Français", "French_Pointing_Dog.png"),
                Map.entry("Barbet", "Barbet.png"),
                Map.entry("Coton de Tuléar", "Coton_de_Tulear.png"),
                Map.entry("Spitz Allemand", "German_Spitz.png"),
                Map.entry("Pékinois", "Pekingese.png"),
                Map.entry("Carlin", "Pug.png"),
                Map.entry("Bull Terrier", "Bull_Terrier.png"),
                Map.entry("Fox Terrier", "Fox_Terrier.png"),
                Map.entry("Scottish Terrier", "Scottish_Terrier.png"),
                Map.entry("Schnauzer", "Schnauzer.png"),
                Map.entry("Airedale Terrier", "Airedale_Terrier.png"),
                Map.entry("Berger des Pyrénées", "Pyrenean_Sheepdog.png"), // TROUVÉ
                Map.entry("Colley à poil long", "Rough_Collie.png"),
                Map.entry("Whippet", "Whippet.png"),
                Map.entry("Lévrier Afghan", "Afghan_Hound.png"),
                Map.entry("Greyhound", "Greyhound.png"),
                Map.entry("Borzoi", "Borzoi.png"),
                Map.entry("Basenji", "Basenji.png"),
                Map.entry("Rhodesian Ridgeback", "Rhodesian_Ridgeback.png"),
                Map.entry("Dogue de Bordeaux", "Dogue_de_Bordeaux.png"),
                Map.entry("Bullmastiff", "Bullmastiff.png"),
                Map.entry("Mastiff", "Mastiff.png"),
                Map.entry("Pinscher Nain", "Miniature_Pinscher.png"), // TROUVÉ
                Map.entry("Chien d'eau Portugais", "Portuguese_Water_Dog.png"), // TROUVÉ
                Map.entry("Lagotto Romagnolo", "Lagotto_Romagnolo.png"), // TROUVÉ
                Map.entry("Berger Picard", "Berger_Picard.png"),
                Map.entry("Beauceron", "Beauceron.png"),
                Map.entry("Berger Hollandais", "Dutch_Shepherd.png"),
                Map.entry("Komondor", "Komondor.png"),
                Map.entry("Kuvasz", "Kuvasz.png"),
                Map.entry("Mâtin des Pyrénées", "Pyrenean_Mastiff.png"),
                Map.entry("Mâtin Espagnol", "Spanish_Mastiff.png"),
                Map.entry("Dogue du Tibet", "Tibetan_Mastiff.png"),
                Map.entry("Fila Brasileiro", "Fila_Brasileiro.png"),
                Map.entry("Akita Américain", "American_Akita.png"),
                Map.entry("Malamute de l'Alaska", "Alaskan_Malamute.png"),
                Map.entry("Groenlandais", "Greenland_Dog.png"),
                Map.entry("Chien Finnois de Laponie", "Finnish_Lapphund.png"),
                Map.entry("Spitz Finlandais", "Finnish_Spitz.png"),
                Map.entry("Spitz des Visigoths", "Swedish_Vallhund.png"), // TROUVÉ
                Map.entry("Chien Norvégien de Macareux", "Norwegian_Lundehund.png"), // TROUVÉ
                Map.entry("Elkhound Norvégien", "Norwegian_Elkhound.png"), // TROUVÉ
                Map.entry("Eurasier", "Eurasier.png"),
                Map.entry("Hovawart", "Hovawart.png"),
                Map.entry("Landseer", "Landseer.png"),
                Map.entry("Retriever à poil plat", "Flat_Coated_Retriever.png"),
                Map.entry("Retriever de la Baie de Chesapeake", "Chesapeake_Bay_Retriever.png"),
                Map.entry("Retriever à poil bouclé", "Curly_Coated_Retriever.png"),
                Map.entry("Retriever de la Nouvelle-Écosse", "Nova_Scotia_Duck_Tolling_Retriever.png"),
                Map.entry("Épagneul d'eau Irlandais", "Irish_Water_Spaniel.png"),
                Map.entry("Épagneul du Tibet", "Tibetan_Spaniel.png"),
                Map.entry("Épagneul Japonais", "Japanese_Chin.png"),
                Map.entry("Épagneul King Charles", "King_Charles_Spaniel.png"),
                Map.entry("Épagneul Papillon", "Papillon.png"),
                Map.entry("Épagneul Phalène", "Phalène.png"),
                Map.entry("Petit Chien Lion", "Löwchen.png"),
                Map.entry("Griffon Belge", "Belgian_Griffon.png"),
                Map.entry("Griffon Bruxellois", "Brussels_Griffon.png"),
                Map.entry("Petit Brabançon", "Petit_Brabançon.png"),
                Map.entry("Terrier Irlandais", "Irish_Terrier.png"),
                Map.entry("Terrier Kerry Blue", "Kerry_Blue_Terrier.png"),
                Map.entry("Terrier Soft Coated Wheaten", "Soft_Coated_Wheaten_Terrier.png"),
                Map.entry("Terrier Tibétain", "Tibetan_Terrier.png"),
                Map.entry("Terrier Noir Russe", "Black_Russian_Terrier.png"),
                Map.entry("Bedlington Terrier", "Bedlington_Terrier.png"),
                Map.entry("Border Terrier", "Border_Terrier.png"),
                Map.entry("Cairn Terrier", "Cairn_Terrier.png"),
                Map.entry("Dandie Dinmont Terrier", "Dandie_Dinmont_Terrier.png"),
                Map.entry("Lakeland Terrier", "Lakeland_Terrier.png"),
                Map.entry("Manchester Terrier", "Manchester_Terrier.png"),
                Map.entry("Norfolk Terrier", "Norfolk_Terrier.png"),
                Map.entry("Norwich Terrier", "Norwich_Terrier.png"),
                Map.entry("Sealyham Terrier", "Sealyham_Terrier.png"),
                Map.entry("Skye Terrier", "Skye_Terrier.png"),
                Map.entry("Terrier Australien", "Australian_Terrier.png"), // TROUVÉ
                Map.entry("Terrier Tchèque", "Cesky_Terrier.png"),
                Map.entry("Terrier Japonais", "Japanese_Terrier.png"),
                Map.entry("Terrier Brésilien", "Brazilian_Terrier.png"),
                Map.entry("Basset Artésien Normand", "Artesian_Norman_Basset.png"), // TROUVÉ
                Map.entry("Basset Bleu de Gascogne", "Blue_Gascony_Basset.png"), // TROUVÉ
                Map.entry("Basset Fauve de Bretagne", "Fawn_Brittany_Basset.png"), // TROUVÉ
                Map.entry("Grand Basset Griffon Vendéen", "Grand_Basset_Griffon_Vendéen.png"), // TROUVÉ
                Map.entry("Petit Basset Griffon Vendéen", "Petit_Basset_Griffon_Vendéen.png"), // TROUVÉ
                Map.entry("Briquet Griffon Vendéen", "Briquet_Griffon_Vendéen.png"), // TROUVÉ
                Map.entry("Chien d'Artois", "Chien_d'Artois.png"),
                Map.entry("Porcelaine", "Porcelaine.png"),
                Map.entry("Billy", "Billy.png"),
                Map.entry("Français Blanc et Noir", "French_White_and_Black_Hound.png"), // TROUVÉ
                Map.entry("Français Tricolore", "French_Tricolour_Hound.png"), // TROUVÉ
                Map.entry("Français Blanc et Orange", "French_White_and_Orange_Hound.png"), // TROUVÉ
                Map.entry("Grand Anglo-Français Tricolore", "Grand_Anglo_French_Tricolour.png"), // TROUVÉ
                Map.entry("Grand Anglo-Français Blanc et Noir", "Grand_Anglo_French_White_Black.png"), // TROUVÉ
                Map.entry("Grand Anglo-Français Blanc et Orange", "Grand_Anglo_French_White_Orange.png"), // TROUVÉ
                Map.entry("Grand Bleu de Gascogne", "Grand_Bleu_de_Gascogne.png"), // TROUVÉ
                Map.entry("Grand Gascon Saintongeois", "Grand_Gascon_Saintongeois.png"), // TROUVÉ
                Map.entry("Griffon Bleu de Gascogne", "Blue_Gascony_Griffon.png"), // TROUVÉ
                Map.entry("Griffon Fauve de Bretagne", "Fawn_Brittany_Griffon.png"), // TROUVÉ
                Map.entry("Griffon Nivernais", "Nivernais_Griffon.png"), // TROUVÉ
                Map.entry("Poitevin", "Poitevin.png"),
                Map.entry("Chien de Saint-Hubert", "Bloodhound.png"),
                Map.entry("Sloughi", "Sloughi.png"),
                Map.entry("Azawakh", "Azawakh.png"),
                Map.entry("Galgo Espagnol", "Galgo_Español.png"),
                Map.entry("Irish Wolfhound", "Irish_Wolfhound.png"),
                Map.entry("Deerhound", "Scottish_Deerhound.png"), // TROUVÉ
                Map.entry("Petit Lévrier Italien", "Italian_Greyhound.png"),
                Map.entry("Saluki", "Saluki.png")
        );

        return breedToFile.getOrDefault(breedName, "placeholder_no_breed.jpg");
    }
}