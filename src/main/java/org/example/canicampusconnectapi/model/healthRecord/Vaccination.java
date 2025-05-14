package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;

import java.util.Date;

@Getter
@Setter
@Entity
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccination_id")
    protected Long id;

    @Column(nullable = false)
    protected Date vaccinationDate;

    @Column(length = 255, nullable = true)
    protected String batchNumber;

    @Column(length = 255, nullable = true)
    protected String veterinarian;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    @JsonView(OwnerView.class)
    private Dog dog;

    @ManyToOne
    @JoinColumn(name = "vaccine_id", nullable = false)
    @JsonView(OwnerView.class)
    protected Vaccine vaccine;
}
