package org.example.canicampusconnectapi.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String title;

    @Column(columnDefinition = "TEXT")
    protected String description;

    @Column(columnDefinition = "DATETIME")
    protected String startDatetime;
    @Column(columnDefinition = "DATETIME")
    protected String endDatetime;

    @Column(nullable = false)
    protected int maxCapacity;
    protected int reservedCapacity;

    @Column(nullable = false)
    protected float price;

    @ManyToOne
    @JoinColumn(name = "club_id"
//            nullable = false
    )
    protected Club club;

    @ManyToOne
    @JoinColumn(name = "coach_id"
//            nullable = false
    )
    protected Coach coach;

    @ManyToOne
    @JoinColumn(name = "course_type_id"
//            nullable = false
            )
    protected CourseType courseType;
}
