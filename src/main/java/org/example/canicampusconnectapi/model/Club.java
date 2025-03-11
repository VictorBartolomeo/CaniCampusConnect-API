package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String club_name;
    protected String club_address;

    @ManyToOne
    protected Department department_id;

}
