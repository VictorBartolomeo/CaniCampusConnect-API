package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@Entity
public class MedicationTreatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    protected Long id;

    @Column(name = "medication_name", nullable = false, length = 255)
    protected String name;

    @Column(length = 255)
    protected String dosage;

    protected Time frequency;

    @Column(nullable = false)
    protected Date startDate;

    @Column(nullable = true)
    protected Date endDate;

    @Column(name = "treatment_reason", length = 255)
    protected String reason;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    @JsonView(OwnerView.class)
    private Dog dog;
}
