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

    @Column
    protected float price;

    @ManyToOne
    protected Club clubId;

    @ManyToOne
    protected Coach userId;

    @ManyToOne
    protected CourseType courseTypeId;
}
