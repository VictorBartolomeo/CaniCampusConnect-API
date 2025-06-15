package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.enumeration.MassUnit;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.example.canicampusconnectapi.view.utilities.WeightView;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
public class DogWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(WeightView.class)
    protected Long id;

    @Column(nullable = false)
    @JsonView({OwnerViewDog.class, WeightView.class})
    protected Date measurementDate;

    @Column(precision = 5, scale = 2, nullable = false)
    @DecimalMin(value = "0.1")
    @JsonView({OwnerViewDog.class,WeightView.class})
    protected BigDecimal weightValue;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    @JsonView({OwnerViewDog.class,WeightView.class})
    protected MassUnit unit;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonView(WeightView.class)
    private Dog dog;
}
