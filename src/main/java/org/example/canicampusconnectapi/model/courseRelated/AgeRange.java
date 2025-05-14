package org.example.canicampusconnectapi.model.courseRelated;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import java.util.List;

@Getter
@Setter
@Entity
public class AgeRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_range_id")
    protected Long id;

    // En mois
    @Column(nullable = false)
    protected int minAge;

    //En mois
    @Column(nullable = false)
    protected int maxAge;

    @OneToMany(mappedBy = "ageRange")
    @JsonView(AdminView.class)
    protected List<CourseType> courseTypes;
}
