package org.example.canicampusconnectapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

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

}
