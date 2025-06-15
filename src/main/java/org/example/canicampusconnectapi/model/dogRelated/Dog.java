package org.example.canicampusconnectapi.model.dogRelated;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
import org.example.canicampusconnectapi.serializer.BreedOrderSerializer;
import org.example.canicampusconnectapi.view.admin.AdminViewDog;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.example.canicampusconnectapi.view.utilities.WeightView;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "dog")
@RgpdEntity(identifierField = "id")
@EntityListeners(AuditingEntityListener.class)
public class Dog {

    public interface updateFromOwner {
    }

    public interface CreateFromOwner {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, AdminViewDog.class,WeightView.class})
    protected Long id;

    @PersonalData(anonymizeWith = "Anonymized Dog")
    @Column(nullable = false, length = 255)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class,WeightView.class})
    protected String name;

    @Column(nullable = false)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class,WeightView.class})
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateFromOwner.class})
    @Column(nullable = false)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class,WeightView.class})
    protected Gender gender;

    @PersonalData(anonymizeWith = "000-000-000")
    @Column(unique = true, length = 50, nullable = true)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewDog.class,WeightView.class})
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
    @OrderColumn(name = "breed_order") // ✅ Préserve l'ordre en base
    @JsonSerialize(using = BreedOrderSerializer.class)
    @NotNull(groups = {CreateFromOwner.class, updateFromOwner.class, CoachView.class})
    @JsonView({OwnerViewDog.class})
    private List<Breed> breeds = new ArrayList<>();


    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class})
    private List<Registration> registrations;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class})
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class})
    private List<VeterinaryVisit> veterinaryVisits;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private List<MedicationTreatment> medicationTreatments;

    @OneToMany(mappedBy = "dog")
    @JsonView({OwnerViewDog.class, AdminViewDog.class})
    private List<DogWeight> dogWeights;

    @Column(nullable = false)
    private boolean isAnonymized = false;

    @Column
    @LastModifiedDate
    private LocalDateTime anonymizedAt;

    @Column
    @LastModifiedBy
    private String anonymizedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView({AdminViewDog.class})
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonView({AdminViewDog.class})
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 150)
    @JsonView({AdminViewDog.class})
    private String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 150)
    @JsonView({AdminViewDog.class})
    private String lastModifiedBy;

    public boolean isActive() {
        return !this.isAnonymized;
    }
}
