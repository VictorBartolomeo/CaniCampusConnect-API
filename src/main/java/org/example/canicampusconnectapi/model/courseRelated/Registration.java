package org.example.canicampusconnectapi.model.courseRelated;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dog_id", "course_id"})
})
public class Registration {

    public interface RegistrationStatusValidation{}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    @JsonView({CoachView.class, CoachViewRegistrations.class})
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Le cours ne peut pas être vide")
    @JsonView({OwnerViewDog.class,CoachViewRegistrations.class})
    protected Course course;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    @NotNull(message = "Le chien ne peut pas être vide")
    @JsonView({OwnerViewCourse.class, CoachView.class,CoachViewRegistrations.class})
    protected Dog dog;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class,CoachView.class,CoachViewRegistrations.class})
    protected Instant registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 255)
    @NotNull(message = "Le statut ne peut pas être vide",
            groups = {RegistrationStatusValidation.class})
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class,CoachView.class,CoachViewRegistrations.class})
    protected RegistrationStatus status;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 150)
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    private String createdBy;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    private LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @Column(nullable = false, length = 150)
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    private String lastModifiedBy;
}
