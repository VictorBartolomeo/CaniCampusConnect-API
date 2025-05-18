package org.example.canicampusconnectapi.model.courseRelated;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.enumeration.RegistrationStatus;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

/**
 * Represents a registration of a dog for a course.
 * This is the main entity for the business logic of the application.
 */
@Getter
@Setter
@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Le cours ne peut pas être vide")
    @JsonView(OwnerViewDog.class)
    protected Course course;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    @NotNull(message = "Le chien ne peut pas être vide")
    protected Dog dog;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView(OwnerViewDog.class)
    protected LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 255)
    @NotNull(message = "Le statut ne peut pas être vide")
    @JsonView(OwnerViewDog.class)
    protected RegistrationStatus status;

}
