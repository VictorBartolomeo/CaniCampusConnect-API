package org.example.canicampusconnectapi.model.dogRelated;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.Gender;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.model.healthRecord.MedicationTreatment;
import org.example.canicampusconnectapi.model.healthRecord.Vaccination;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.example.canicampusconnectapi.model.users.Owner;

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
    @JsonBackReference("owner-dogs")
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "dog_breed",
            joinColumns = @JoinColumn(name = "dog_id"),
            inverseJoinColumns = @JoinColumn(name = "breed_id")
    )
    @JsonIgnoreProperties("dogs")
    private Set<Breed> breeds;


    @OneToMany(mappedBy = "dog")
    @JsonIgnore
//    @JsonManagedReference("dog-registrations")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference("dog-vaccinations")
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference("dog-veterinaryVisits")
    private List<VeterinaryVisit> veterinaryVisits;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference("dog-medicationTreatments")
    private List<MedicationTreatment> medicationTreatments;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference("dog-dogWeights")
    private List<DogWeight> dogWeights;



}
