package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class User {

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
    @Column(nullable = false)
    protected String role;

    @Column(nullable = true)
    protected String phone;

}
