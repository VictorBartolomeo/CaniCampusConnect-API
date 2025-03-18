package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@Entity
public class VeterinaryVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Date visit_date;
    @Column(columnDefinition = "TEXT")
    protected String diagnosis;
    protected String reason_for_visit;

    @Column(columnDefinition = "TEXT")
    protected String treatment;

    protected String veterinarian;

    @ManyToOne
    protected Dog dog;

}
