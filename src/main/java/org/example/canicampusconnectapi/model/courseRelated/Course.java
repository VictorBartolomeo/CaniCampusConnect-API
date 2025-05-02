package org.example.canicampusconnectapi.model.courseRelated;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.Club;
import org.example.canicampusconnectapi.model.users.Coach;

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
    @JsonBackReference
    protected Coach coach;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonBackReference
    protected Club club;

    @ManyToOne
    @JoinColumn(name = "course_type_id", nullable = false)
    @JsonBackReference
    protected CourseType courseType;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference
    protected List<Registration> registrations;

}
