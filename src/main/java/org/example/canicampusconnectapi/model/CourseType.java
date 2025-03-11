package org.example.canicampusconnectapi.model;


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

    protected String course_name;

    @Column(columnDefinition = "TEXT")
    protected String description;

    @ManyToOne
    protected Course course_id;

    @ManyToOne
    protected AgeRange age_range_id;


}
