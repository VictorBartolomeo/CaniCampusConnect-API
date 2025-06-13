package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.admin.AdminViewCoach;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "user_id")
public class Coach extends User {

    public interface onCreateCoach{}
    public interface onUpdateCoach{}

    @Column(nullable = false, length = 100)
    @JsonView({CoachView.class, OwnerView.class, AdminViewCoach.class})
    @NotBlank(message = "le numéro ACACED doit être renseigné", groups = {onCreateCoach.class, onUpdateCoach.class})
    protected String acacedNumber;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @JsonView({AdminViewCoach.class})
    protected boolean isActive;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView({CoachView.class, AdminViewCoach.class})
    protected LocalDate registrationDate;

    @OneToMany(mappedBy = "coach")
    @JsonView({AdminViewCoach.class})
    protected List<Course> course;
}
