package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id")
    protected Integer id;

    @Column(name = "vaccine_name", nullable = false, length = 255)
    protected String name;

    @Column(nullable = false)
    protected short renewDelay;

    @OneToMany(mappedBy = "vaccine")
    @JsonBackReference("vaccine-vaccinations")
    private List<Vaccination> vaccinations;

}
