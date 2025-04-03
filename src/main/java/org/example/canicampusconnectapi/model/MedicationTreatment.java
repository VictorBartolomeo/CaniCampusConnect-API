package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@Entity
public class MedicationTreatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String medicationName;
    protected String dosage;
    protected Time frequency;
    protected Date startDate;
    protected Date endDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Dog dog;

    @Column(columnDefinition = "TEXT")
    protected String treatmentReason;

}
