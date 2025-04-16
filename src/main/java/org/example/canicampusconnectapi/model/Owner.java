package org.example.canicampusconnectapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@EntityListeners(AuditingEntityListener.class)
public class Owner extends User {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDate registrationDate;

    @ColumnDefault("TRUE")
    @Column(nullable = false)
    protected boolean isActive;

    @Column(nullable = false, length = 255)
    protected String address;

}

