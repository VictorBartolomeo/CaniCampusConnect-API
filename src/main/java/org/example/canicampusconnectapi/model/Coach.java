package org.example.canicampusconnectapi.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Coach extends User{

protected String acacedNumber;
protected boolean isActive;
protected LocalDate registrationDate;

}
