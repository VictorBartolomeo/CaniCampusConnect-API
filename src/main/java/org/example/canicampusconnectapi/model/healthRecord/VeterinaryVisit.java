package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;

import java.util.Date;

@Getter
@Setter
@Entity
public class VeterinaryVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    protected Long id;

    @Column(name = "visit_date", nullable = false)
    @JsonView(OwnerViewDog.class)
    protected Date visitDate;

    @Column(columnDefinition = "TEXT")
    @JsonView(OwnerViewDog.class)
    protected String diagnosis;

    @Column(length = 255)
    @JsonView(OwnerViewDog.class)
    protected String reasonForVisit;

    @Column(columnDefinition = "TEXT")
    @JsonView(OwnerViewDog.class)
    protected String treatment;

    @Column(length = 255)
    @JsonView(OwnerViewDog.class)
    protected String veterinarian;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

}
