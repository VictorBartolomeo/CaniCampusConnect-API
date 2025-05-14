package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;

import java.util.List;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class ClubOwner extends User {

    @OneToMany(mappedBy = "clubOwner")
    @JsonView(AdminView.class)
    protected List<Club> clubs;
}
