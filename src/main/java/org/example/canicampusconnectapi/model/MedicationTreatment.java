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

    protected String medication_name;
    protected String dosage;
    protected Time frequency;
    protected Date start_date;
    protected Date end_date;

    @Column(columnDefinition = "TEXT")
    protected String treatment_reason;

}
