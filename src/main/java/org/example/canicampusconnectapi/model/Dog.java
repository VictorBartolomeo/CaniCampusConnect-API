package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;
    private LocalDate birthDate;
    protected boolean isMale;

    //TODO Demander à Franck si c'est pertinent
    protected boolean is_sociable;
    protected boolean is_in_heat;

    //TODO Demander à Franck comment activer la seconde cardinalité
    protected boolean isCrossbreeded;

    protected String chip_number;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Breed breed;

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
