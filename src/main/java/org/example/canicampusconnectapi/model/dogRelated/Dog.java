package org.example.canicampusconnectapi.model.dogRelated;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.example.canicampusconnectapi.model.courseRelated.Registration;
import org.example.canicampusconnectapi.model.enumeration.Gender;
import org.example.canicampusconnectapi.model.healthRecord.DogWeight;
import org.example.canicampusconnectapi.model.healthRecord.MedicationTreatment;
import org.example.canicampusconnectapi.model.healthRecord.Vaccination;
import org.example.canicampusconnectapi.model.healthRecord.VeterinaryVisit;
import org.example.canicampusconnectapi.model.users.Owner;
import org.example.canicampusconnectapi.security.annotation.rgpd.PersonalData;
import org.example.canicampusconnectapi.security.annotation.rgpd.RgpdEntity;
import org.example.canicampusconnectapi.view.admin.AdminViewDog;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dog")
@RgpdEntity(identifierField = "id")
@Where(clause = "is_anonymized = false")
@EntityListeners(AuditingEntityListener.class)
public class Dog {

    public interface updateFromOwner {
    }

    public interface CreateFromOwner {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, AdminViewDog.class})
    protected Long id;

    @PersonalData(anonymizeWith = "Anonymized Dog")
    @Column(nullable = false, length = 255)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class})
    protected String name;

    @Column(nullable = false)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class})
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateFromOwner.class})
    @Column(nullable = false)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class})
    protected Gender gender;

    @PersonalData(nullable = true)
    @Column(unique = true, length = 50)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class})
    protected String chipNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachViewRegistrations.class, AdminViewDog.class})
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "dog_breed",
            joinColumns = @JoinColumn(name = "dog_id"),
            inverseJoinColumns = @JoinColumn(name = "breed_id")
    )
    @NotNull(groups = {CreateFromOwner.class, updateFromOwner.class, CoachView.class})
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private Set<Breed> breeds;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private List<Registration> registrations;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private List<VeterinaryVisit> veterinaryVisits;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private List<MedicationTreatment> medicationTreatments;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private List<DogWeight> dogWeights;

    // Champs d'audit pour l'anonymisation
    @Column(nullable = false)
    private boolean isAnonymized = false;

    @Column
    @LastModifiedDate
    private LocalDateTime anonymizedAt;

    @Column
    @LastModifiedBy
    private String anonymizedBy;

    public boolean isActive() {
        return !this.isAnonymized;
    }
}