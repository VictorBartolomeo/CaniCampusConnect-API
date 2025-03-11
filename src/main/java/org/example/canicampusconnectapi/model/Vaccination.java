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

    protected Date vaccination_date;
    protected Date reminder_date;
    protected String batch_number;
    protected String veterinarian;

    @ManyToMany
    @JoinTable(
            name = "vaccine_vaccination",
            joinColumns = @JoinColumn(name = "vaccination_id"),
            inverseJoinColumns = @JoinColumn(name = "vaccine_id")
    )
    protected List<Vaccination> vaccinations = new ArrayList<>();




}
