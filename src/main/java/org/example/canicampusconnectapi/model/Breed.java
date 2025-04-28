package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(length = 100, unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "breeds", fetch = FetchType.LAZY) // mappedBy fait référence au champ "breeds" dans Dog
    private Set<Dog> dogs; // Utilisation de Set



}
