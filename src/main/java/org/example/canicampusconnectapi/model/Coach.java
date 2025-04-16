package org.example.canicampusconnectapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "user_id")
public class Coach extends User{

protected String acacedNumber;
protected boolean isActive;
protected LocalDate registrationDate;

}
