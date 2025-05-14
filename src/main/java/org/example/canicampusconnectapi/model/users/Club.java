package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;

import java.util.List;

@Entity
@Getter
@Setter
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    protected Integer id;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Le nom ne peut pas être vide")
    protected String name;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "L'adresse ne peut pas être vide")
    protected String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(AdminView.class)
    protected ClubOwner clubOwner;

    @OneToMany
    @JoinColumn(name = "club_id")
    @JsonView(AdminView.class)
    protected List<Course> courses;
}
