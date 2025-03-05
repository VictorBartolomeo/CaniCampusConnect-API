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

    @Column(nullable = false)
    protected int course_type_id;

    protected String title;

    @Column(columnDefinition = "TEXT")
    protected String description;


    @Column(columnDefinition = "DATETIME")
    protected String start_datetime;
    @Column(columnDefinition = "DATETIME")
    protected String end_datetime;

    @Column(nullable = false)
    protected int max_capacity;
    protected int reserved_capacity;

    @Column(columnDefinition = "CURRENCY(EUR)")
    protected double price;

    @ManyToOne
    protected Club club_id;

    @ManyToOne
    protected Coach coach_id;

}
