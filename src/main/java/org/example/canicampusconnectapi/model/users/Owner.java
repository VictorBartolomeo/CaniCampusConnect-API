package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@EntityListeners(AuditingEntityListener.class)
public class Owner extends User {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView(OwnerViewDog.class)
    protected LocalDate registrationDate;

    @ColumnDefault("TRUE")
    @Column(nullable = false)
    protected boolean isActive;

    @JsonView(OwnerViewDog.class)
    protected String address;

    @OneToMany(mappedBy ="owner")
    //TODO Ici récursivité, comment l'éviter ?
    protected List<Dog> dogs;

}
