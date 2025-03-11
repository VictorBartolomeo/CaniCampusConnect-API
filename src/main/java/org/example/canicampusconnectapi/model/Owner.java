package org.example.canicampusconnectapi.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Owner extends User {

    protected Date registration_date;
    protected boolean isActive;
    protected String Address;

}
