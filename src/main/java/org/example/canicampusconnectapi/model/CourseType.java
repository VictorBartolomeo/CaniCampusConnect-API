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
    protected Long id;

    protected String name;

    @Column(columnDefinition = "TEXT")
    protected String description;

    @OneToMany
    protected List<Course> courses;

    @ManyToOne
    protected AgeRange ageRange;


}
