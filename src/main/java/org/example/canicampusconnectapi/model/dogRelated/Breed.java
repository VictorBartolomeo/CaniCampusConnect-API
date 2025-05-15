package org.example.canicampusconnectapi.model.dogRelated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;

import java.util.Set;

@Getter
@Setter
@Entity
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Short id;

    @Column(length = 100, unique = true, nullable = false)
    @JsonView(OwnerViewDog.class)
    protected String name;

    @ManyToMany(mappedBy = "breeds", fetch = FetchType.LAZY)
    protected Set<Dog> dogs;

    String avatarUrl;


}
