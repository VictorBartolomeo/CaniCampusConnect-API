package org.example.canicampusconnectapi.model.courseRelated;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.example.canicampusconnectapi.view.utilities.AgeRangeView;

@Getter
@Setter
@Entity
public class CourseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_type_id")
    @JsonView(AdminView.class)
    protected Long id;

    @Column(nullable = false, length = 255)
    @NotNull(message = "Le nom du type de cours ne peut être vide", groups = Course.CreateCourse.class)
    @JsonView({OwnerViewDog.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AgeRangeView.class})
    protected String name;

    @Column(columnDefinition = "TEXT")
    @JsonView({OwnerViewDog.class,OwnerViewCourse.class,CoachView.class})
    protected String description;

    @OneToMany(mappedBy = "courseType")
    protected List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "age_range_id", nullable = false)
    @NotNull(message = "Le tranche d'age doit être renseignée", groups = Course.CreateCourse.class)
    @JsonView({OwnerViewDog.class,OwnerViewCourse.class,CoachView.class,CoachViewRegistrations.class})
    protected AgeRange ageRange;


}
