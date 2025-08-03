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
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.example.canicampusconnectapi.view.utilities.CourseTypeView;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a course offered by a club and taught by a coach.
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Course {

    public interface CreateCourse {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, CourseTypeView.class})
    protected Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Le titre ne peut pas être vide", groups = {CreateCourse.class})
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, CourseTypeView.class})
    protected String title;

    @Column(columnDefinition = "TEXT")
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    protected String description;

    @Column(nullable = false)
    @NotNull(message = "La date et l'heure de début ne peuvent pas être vides", groups = {CreateCourse.class})
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    protected Instant startDatetime;

    @Column(nullable = false)
    @NotNull(message = "La date et l'heure de fin ne peuvent pas être vides", groups = {CreateCourse.class})
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    protected Instant endDatetime;

    @Column(nullable = false)
    @NotNull(message = "Vous devez renseigner un nombre de place limitées", groups = {CreateCourse.class})
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    protected int maxCapacity;

    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Vous devez renseigner un coach", groups = {CreateCourse.class})
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachViewRegistrations.class})
    protected Coach coach;

    //Sera toujours 1 via le PrePersist et le PreUpdate, je ne gère pas encore les multiples clubs
    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @NotNull(message = "Vous devez renseigner un club") //Retiré de l'interface validated OnCreateCourse
    @JsonIgnore //Le club ne m'interesse pas pour le moment car pas de gestion de clubs multiples
    protected Club club;

    @ManyToOne
    @JoinColumn(name = "course_type_id", nullable = false)
    @NotNull(message = "Vous devez renseigner un type de cours", groups = {CreateCourse.class})
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class})
    protected CourseType courseType;

    @OneToMany(mappedBy = "course")
    @JsonView({CoachView.class, OwnerViewCourse.class})
    protected List<Registration> registrations;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView({OwnerViewCourse.class, CoachView.class, AdminViewCourse.class})
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonView({OwnerViewCourse.class, CoachView.class, AdminViewCourse.class})
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 150)
    @JsonView({OwnerViewCourse.class, CoachView.class, AdminViewCourse.class})
    private String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 150)
    @JsonView({OwnerViewCourse.class, CoachView.class, AdminViewCourse.class})
    private String lastModifiedBy;

    @PrePersist
    private void setDefaultClub() {
        if (this.club == null) {
            Club defaultClub = new Club();
            defaultClub.setId(1);
            this.club = defaultClub;
        }
    }

    @PreUpdate
    private void ensureDefaultClub() {
        Club defaultClub = new Club();
        defaultClub.setId(1);
        this.club = defaultClub;
    }

}
