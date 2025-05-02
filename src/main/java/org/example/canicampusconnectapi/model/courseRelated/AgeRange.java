package org.example.canicampusconnectapi.model.courseRelated;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
    @JsonManagedReference
    protected List<CourseType> courseTypes;
}
