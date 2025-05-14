package org.example.canicampusconnectapi.model.dogRelated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
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
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(AdminView.class)
    protected Long id;

    @Column(nullable = false, length = 255)
    @JsonView(OwnerView.class)
    protected String name;

    @Column(nullable = false)
    @JsonView(OwnerView.class)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonView(OwnerView.class)
    protected Gender gender;

    @Column(unique = true, length = 50)
    @JsonView(CoachView.class)
    protected String chipNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(OwnerView.class)
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "dog_breed",
            joinColumns = @JoinColumn(name = "dog_id"),
            inverseJoinColumns = @JoinColumn(name = "breed_id")
    )
    @JsonIgnoreProperties("dogs")
    @JsonView(OwnerView.class)
    private Set<Breed> breeds;


    @OneToMany(mappedBy = "dog")
    @JsonIgnore
//    @JsonManagedReference("dog-registrations")
    private List<Registration> registrations;

    @OneToMany(mappedBy = "dog")
    @JsonView(OwnerView.class)
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "dog")
    @JsonView(OwnerView.class)
    private List<VeterinaryVisit> veterinaryVisits;

    @OneToMany(mappedBy = "dog")
    @JsonView(OwnerView.class)
    private List<MedicationTreatment> medicationTreatments;

    @OneToMany(mappedBy = "dog")
    @JsonView(OwnerView.class)
    private List<DogWeight> dogWeights;



}
