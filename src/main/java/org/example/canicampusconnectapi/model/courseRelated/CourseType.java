package org.example.canicampusconnectapi.model.courseRelated;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference("courseType-courses")
    protected List<Course> courses;

    @ManyToOne
    @JoinColumn(name = "age_range_id", nullable = false)
    @JsonBackReference("ageRange-courseTypes")
    protected AgeRange ageRange;


}
