package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class DogWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Date measurement_date;

    @DecimalMin(value = "0.1")
    protected float weight;
    protected MassUnit mass_unit;
}
