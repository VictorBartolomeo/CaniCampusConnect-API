package org.example.canicampusconnectapi.model.healthRecord;

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
    protected Date renewDelay;

    @OneToMany(mappedBy = "vaccine")
    private List<Vaccination> vaccinations;

}
