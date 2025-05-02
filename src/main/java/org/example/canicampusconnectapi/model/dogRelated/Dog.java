package org.example.canicampusconnectapi.model.dogRelated;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
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
    @JsonBackReference
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "dog_breed",
            joinColumns = @JoinColumn(name = "dog_id"),
            inverseJoinColumns = @JoinColumn(name = "breed_id")
    )
    private Set<Breed> breeds;


    @OneToMany(mappedBy = "dog")
    @JsonManagedReference
    private List<Registration> registrations;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference
    private List<VeterinaryVisit> veterinaryVisits;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference
    private List<MedicationTreatment> medicationTreatments;

    @OneToMany(mappedBy = "dog")
    @JsonManagedReference
    private List<DogWeight> dogWeights;


}
