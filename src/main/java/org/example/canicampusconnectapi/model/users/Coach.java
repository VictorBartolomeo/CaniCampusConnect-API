package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.courseRelated.Course;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "user_id")
public class Coach extends User {

    @Column(nullable = true, length = 100)
    protected String acacedNumber;

    @Column(nullable = false, columnDefinition = "boolean default true")
    protected boolean isActive;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDate registrationDate;

    @OneToMany(mappedBy = "coach")
    @JsonBackReference("coach-courses")
    protected List<Course> course;
}
