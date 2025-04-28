package org.example.canicampusconnectapi.model.healthRecord;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;

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
    protected Date visitDate;

    @Column(columnDefinition = "TEXT")
    protected String diagnosis;

    @Column(length = 255)
    protected String reasonForVisit;

    @Column(columnDefinition = "TEXT")
    protected String treatment;

    @Column(length = 255)
    protected String veterinarian;

    @ManyToOne
    @JoinColumn(name = "dog_id", nullable = false)
    private Dog dog;

}
