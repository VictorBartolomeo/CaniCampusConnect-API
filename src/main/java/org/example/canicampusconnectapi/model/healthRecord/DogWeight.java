package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.model.enumeration.MassUnit;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class DogWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected LocalDateTime measurementDate;

    @Column(precision = 5, scale = 2, nullable = false)
    @DecimalMin(value = "0.1")
    protected BigDecimal weightValue;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    protected MassUnit unit;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonView(OwnerView.class)
    private Dog dog;
}
