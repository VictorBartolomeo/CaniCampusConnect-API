package org.example.canicampusconnectapi.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Le cours ne peut pas être vide")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    @NotNull(message = "Le chien ne peut pas être vide")
    private Dog dog;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le statut ne peut pas être vide")
    private RegistrationStatus status;

    /**
     * Checks if the registration is for an upcoming course.
     * @return true if the course start date is in the future, false otherwise
     */
    public boolean isUpcoming() {
        return course.getStartDatetime().isAfter(LocalDateTime.now());
    }

    /**
     * Checks if the registration is for a past course.
     * @return true if the course end date is in the past, false otherwise
     */
    public boolean isPast() {
        return course.getEndDatetime().isBefore(LocalDateTime.now());
    }

    /**
     * Checks if the registration is for a current course.
     * @return true if the course is currently ongoing, false otherwise
     */
    public boolean isCurrent() {
        LocalDateTime now = LocalDateTime.now();
        return course.getStartDatetime().isBefore(now) && course.getEndDatetime().isAfter(now);
    }
}
