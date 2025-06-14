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
import org.example.canicampusconnectapi.view.utilities.AgeRangeView;

import java.util.List;

@Getter
@Setter
@Entity
public class AgeRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_range_id")
    @JsonView({OwnerViewCourse.class, AgeRangeView.class})
    protected Long id;

    // En mois
    @Column(nullable = false)
    @JsonView({OwnerView.class, OwnerViewCourse.class,CoachView.class, CoachViewRegistrations.class, AgeRangeView.class})
    protected int minAge;

    //En mois
    @Column(nullable = false)
    @JsonView({OwnerView.class,OwnerViewCourse.class,CoachView.class,CoachViewRegistrations.class, AgeRangeView.class})
    protected int maxAge;

    @Column(nullable = false)
    @JsonView({OwnerView.class,OwnerViewCourse.class,CoachView.class,CoachViewRegistrations.class, AgeRangeView.class})
    protected String name;

    @OneToMany(mappedBy = "ageRange")
    @JsonView(AgeRangeView.class)
    protected List<CourseType> courseTypes;
}
