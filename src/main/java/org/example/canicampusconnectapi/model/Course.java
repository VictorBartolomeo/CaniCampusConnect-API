package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a course offered by a club and taught by a coach.
 */
@Getter
@Setter
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    protected Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Le titre ne peut pas être vide")
    protected String title;

    @Column(columnDefinition = "TEXT")
    protected String description;

    @Column(nullable = false)
    @NotNull(message = "La date et l'heure de début ne peuvent pas être vides")
    protected LocalDateTime startDatetime;

    @Column(nullable = false)
    @NotNull(message = "La date et l'heure de fin ne peuvent pas être vides")
    protected LocalDateTime endDatetime;

    @Column(nullable = false)
    protected int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    protected Coach coach;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    protected Club club;

    @ManyToOne
    @JoinColumn(name = "course_type_id", nullable = false)
    protected CourseType courseType;

    @OneToMany(mappedBy = "course")
    protected List<Registration> registrations;

    /**
     * Checks if the course is upcoming.
     * @return true if the course start date is in the future, false otherwise
     */
    public boolean isUpcoming() {
        return startDatetime.isAfter(LocalDateTime.now());
    }

    /**
     * Checks if the course is past.
     * @return true if the course end date is in the past, false otherwise
     */
    public boolean isPast() {
        return endDatetime.isBefore(LocalDateTime.now());
    }

    /**
     * Checks if the course is currently ongoing.
     * @return true if the course is currently ongoing, false otherwise
     */
    public boolean isCurrent() {
        LocalDateTime now = LocalDateTime.now();
        return startDatetime.isBefore(now) && endDatetime.isAfter(now);
    }

    /**
     * Gets the number of registrations for this course.
     * @return the number of registrations
     */
    public int getRegistrationCount() {
        return registrations != null ? registrations.size() : 0;
    }

    /**
     * Checks if the course is full.
     * @return true if the number of registrations is greater than or equal to the maximum capacity, false otherwise
     */
    public boolean isFull() {
        return getRegistrationCount() >= maxCapacity;
    }
}
