package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.security.annotation.rgpd.PersonalData;
import org.example.canicampusconnectapi.security.annotation.rgpd.RgpdEntity;
import org.example.canicampusconnectapi.view.admin.AdminViewCoach;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.coach.CoachViewRegistrations;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@RgpdEntity(identifierField = "id")
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    // GROUPES DE VALIDATION
    public interface OnUpdateFromOwner {}
    public interface OnUpdatePassword {}
    public interface OnUpdateFromAdmin {}

    private static final String REGEX_STRONG_PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,64}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonView({AdminViewCoach.class})
    protected Long id;

    @PersonalData()
    @Column(nullable = false, unique = true, length = 150)
    @NotBlank(message = "L'email ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdateFromOwner.class, OnUpdateFromAdmin.class, Coach.onCreateCoach.class, Coach.onUpdateCoach.class})
    @Email(message = "L'email n'est pas au format valide", groups = {OnUpdateFromAdmin.class, OnUpdateFromOwner.class})
    @JsonView({OwnerViewDog.class, OwnerView.class, CoachView.class, AdminViewCoach.class})
    protected String email;

    @PersonalData(anonymizeWith = "Utilisateur")
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le prénom ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdateFromOwner.class, OnUpdateFromAdmin.class, Coach.onCreateCoach.class, Coach.onUpdateCoach.class})
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewCoach.class})
    protected String firstname;

    @PersonalData(anonymizeWith = "Anonymisé")
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le nom de famille ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdateFromOwner.class, OnUpdateFromAdmin.class, Coach.onCreateCoach.class, Coach.onUpdateCoach.class})
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewCoach.class})
    protected String lastname;

    @Column(nullable = false, length = 255)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Le mot de passe ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdatePassword.class, Coach.onCreateCoach.class})
    @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
    @Pattern(regexp = REGEX_STRONG_PASSWORD, message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial")
    protected String password;

    @PersonalData(anonymizeWith = "PHONE-ANONYMIZED")
    @Column(nullable = true, length = 50)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewCoach.class})
    protected String phone;

    @PersonalData()
    @Column(length = 500)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class, CoachView.class, CoachViewRegistrations.class, AdminViewCoach.class})
    protected String avatarUrl;

    @Column(nullable = false)
    @JsonView({OwnerView.class, CoachView.class, AdminViewCoach.class})
    private boolean emailValidated = false;

    @Column
    @JsonView({AdminViewCoach.class})
    private LocalDateTime emailValidatedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView({AdminViewCoach.class})
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonView({AdminViewCoach.class})
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 150)
    @JsonView({AdminViewCoach.class})
    private String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 150)
    @JsonView({AdminViewCoach.class})
    private String lastModifiedBy;

    @Column(nullable = false)
    @JsonView({AdminViewCoach.class})
    private boolean isAnonymized = false;
}