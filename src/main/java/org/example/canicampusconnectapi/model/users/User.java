package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewCourse;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    public interface OnUpdateFromOwner {
    }
    public interface OnUpdatePassword {}

    private static final String REGEX_STRONG_PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,64}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    protected Long id;

    @Column(nullable = false, unique = true, length = 150)
    @NotBlank(message = "L'email ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdateFromOwner.class})
    @Email(message = "L'email n'est pas au format valide")
    @JsonView({OwnerViewDog.class, OwnerView.class})
    protected String email;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le prénom ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdateFromOwner.class})
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class})
    protected String firstname;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le nom de famille ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdateFromOwner.class})
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class})
    protected String lastname;

    @Column(nullable = false, length = 255) //taille de 255 caractères pour prévenir le mot de passe hashé
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Le mot de passe ne peut pas être vide", groups = {Owner.onCreateOwner.class, OnUpdatePassword.class})
    @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
    @Pattern(regexp = REGEX_STRONG_PASSWORD, message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial")
    protected String password;

    @Column(nullable = true, length = 50)
    @JsonView({OwnerViewDog.class, OwnerView.class, OwnerViewCourse.class})
    protected String phone;

}
