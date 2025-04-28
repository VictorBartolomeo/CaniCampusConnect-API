package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 255)
    protected String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Gender gender;

    @Column(unique = true, length = 50)
    protected String chipNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY) // LAZY est généralement préférable pour les performances
    @JoinTable(
            name = "dog_breed", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "dog_id"), // Clé étrangère vers Dog
            inverseJoinColumns = @JoinColumn(name = "breed_id") // Clé étrangère vers Breed
    )
    private Set<Breed> breeds; // Utilisation de Set pour représenter les races


    @OneToMany(mappedBy = "dog")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "dog")
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "dog")
    private List<VeterinaryVisit> veterinaryVisits;

    @OneToMany(mappedBy = "dog")
    private List<MedicationTreatment> medicationTreatments;

    @OneToMany(mappedBy = "dog")
    private List<DogWeight> dogWeights;


}
