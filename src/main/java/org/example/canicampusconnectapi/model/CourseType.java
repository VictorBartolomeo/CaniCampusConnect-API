package org.example.canicampusconnectapi.model;


import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CourseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_type_id")
    protected Long id;

    @Column(nullable = false, length = 255)
    protected String name;

    @Column(columnDefinition = "TEXT")
    protected String description;

    @OneToMany(mappedBy = "courseType")
    protected List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "age_range_id", nullable = false)
    protected AgeRange ageRange;


}
