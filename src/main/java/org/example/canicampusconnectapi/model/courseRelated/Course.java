package org.example.canicampusconnectapi.model.courseRelated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.users.Club;
import org.example.canicampusconnectapi.model.users.Coach;
import org.example.canicampusconnectapi.view.admin.AdminViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;

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
    @JsonView(AdminViewCourse.class)
    protected Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Le titre ne peut pas être vide")
    @JsonView(OwnerViewCourse.class)
    protected String title;

    @Column(columnDefinition = "TEXT")
    @JsonView(OwnerViewCourse.class)
    protected String description;

    @Column(nullable = false)
    @NotNull(message = "La date et l'heure de début ne peuvent pas être vides")
    @JsonView(OwnerViewCourse.class)
    protected LocalDateTime startDatetime;

    @Column(nullable = false)
    @NotNull(message = "La date et l'heure de fin ne peuvent pas être vides")
    @JsonView(OwnerViewCourse.class)
    protected LocalDateTime endDatetime;

    @Column(nullable = false)
    @JsonView(OwnerViewCourse.class)
    protected int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(OwnerViewCourse.class)
    protected Coach coach;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @JsonView(OwnerViewCourse.class)
    protected Club club;

    @ManyToOne
    @JoinColumn(name = "course_type_id", nullable = false)
    @JsonView(OwnerViewCourse.class)
    protected CourseType courseType;

    @OneToMany(mappedBy = "course")
    @JsonView(AdminViewCourse.class)
    protected List<Registration> registrations;

}
