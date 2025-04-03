package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Coach extends User{

protected String acacedNumber;
protected boolean isActive;
protected Date registrationDate;

}
