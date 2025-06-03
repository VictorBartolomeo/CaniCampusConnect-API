package org.example.canicampusconnectapi.model.courseRelated;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;

import java.util.List;

@Getter
@Setter
@Entity
public class AgeRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_range_id")
    @JsonView(OwnerViewCourse.class)
    protected Long id;

    // En mois
    @Column(nullable = false)
    @JsonView({OwnerView.class, OwnerViewCourse.class,CoachView.class, CoachViewRegistrations.class})
    protected int minAge;

    //En mois
    @Column(nullable = false)
    @JsonView({OwnerView.class,OwnerViewCourse.class,CoachView.class,CoachViewRegistrations.class})
    protected int maxAge;

    @OneToMany(mappedBy = "ageRange")
    protected List<CourseType> courseTypes;
}
