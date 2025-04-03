package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String firstname;
    @Column(nullable = false)
    protected String lastname;
    @Column(nullable = false)
    protected String email;
    @Column(nullable = false)
    protected String password;

    @Column(nullable = true)
    protected String phone;

}
