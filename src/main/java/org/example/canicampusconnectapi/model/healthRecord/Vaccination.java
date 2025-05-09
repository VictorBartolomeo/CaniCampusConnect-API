package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;

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
    @JsonBackReference("dog-vaccinations")
    private Dog dog;

    @ManyToOne
    @JoinColumn(name = "vaccine_id", nullable = false)
    @JsonManagedReference("vaccine-vaccinations")
    protected Vaccine vaccine;
}