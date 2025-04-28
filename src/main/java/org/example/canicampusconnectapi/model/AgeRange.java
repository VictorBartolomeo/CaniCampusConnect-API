package org.example.canicampusconnectapi.model;


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
    protected List<CourseType> courseTypes;
}
