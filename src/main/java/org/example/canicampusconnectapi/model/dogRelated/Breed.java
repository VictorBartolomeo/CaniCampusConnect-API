package org.example.canicampusconnectapi.model.dogRelated;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(OwnerViewDog.class)
    protected Short id;

    @Column(length = 100, unique = true, nullable = false)
    @JsonView({OwnerViewDog.class, CoachView.class})
    protected String name;

    @Column(length = 500)
    @JsonView({OwnerViewDog.class, CoachView.class})
    protected String avatarUrl;

    @ManyToMany(mappedBy = "breeds", fetch = FetchType.LAZY)
    protected List<Dog> dogs;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView(OwnerViewDog.class)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonView(OwnerViewDog.class)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 150)
    @JsonView(OwnerViewDog.class)
    private String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 150)
    @JsonView(OwnerViewDog.class)
    private String lastModifiedBy;
}
