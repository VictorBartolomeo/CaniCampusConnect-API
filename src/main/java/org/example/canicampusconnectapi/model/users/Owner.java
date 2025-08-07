package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.dogRelated.Dog;
import org.example.canicampusconnectapi.security.annotation.rgpd.RgpdEntity;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@RgpdEntity(identifierField = "id")
@PrimaryKeyJoinColumn(name = "user_id")
@EntityListeners(AuditingEntityListener.class)
public class Owner extends User {

    public interface onCreateOwner{
    }

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView({OwnerViewDog.class, OwnerView.class})
    protected Date registrationDate;

    @ColumnDefault("TRUE")
    @Column(nullable = false, name = "is_active")
    @JsonView({OwnerViewDog.class, OwnerView.class})
    protected boolean isActive = true;

    @OneToMany(mappedBy ="owner")
    protected List<Dog> dogs;

}
