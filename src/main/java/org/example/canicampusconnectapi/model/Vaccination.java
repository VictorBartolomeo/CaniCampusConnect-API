package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Date vaccinationDate;
    protected Date reminderDate;
    protected String batchNumber;
    protected String veterinarian;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Dog dog;

    @ManyToMany
    @JoinTable(
            name = "vaccine_vaccination",
            joinColumns = @JoinColumn(name = "vaccination_id"),
            inverseJoinColumns = @JoinColumn(name = "vaccine_id")
    )
    protected List<Vaccination> vaccinations = new ArrayList<>();




}
