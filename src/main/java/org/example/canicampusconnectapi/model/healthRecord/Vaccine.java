package org.example.canicampusconnectapi.model.healthRecord;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;

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
    @JsonView(AdminView.class)
    private List<Vaccination> vaccinations;

}
