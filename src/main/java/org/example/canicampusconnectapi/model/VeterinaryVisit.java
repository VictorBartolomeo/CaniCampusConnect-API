package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class VeterinaryVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Date visitDate;
    @Column(columnDefinition = "TEXT")
    protected String diagnosis;
    protected String reasonForVisit;

    @Column(columnDefinition = "TEXT")
    protected String treatment;

    protected String veterinarian;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Dog dog;

}
