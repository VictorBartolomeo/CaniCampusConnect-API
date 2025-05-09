package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class ClubOwner extends User {

    @OneToMany(mappedBy = "clubOwner")
    @JsonBackReference("club-clubOwner")
    protected List<Club> clubs;
}
