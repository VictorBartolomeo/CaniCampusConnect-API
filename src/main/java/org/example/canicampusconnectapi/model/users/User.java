package org.example.canicampusconnectapi.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.view.admin.AdminView;
import org.example.canicampusconnectapi.view.coach.CoachView;
import org.example.canicampusconnectapi.view.owner.OwnerView;
import org.example.canicampusconnectapi.view.owner.OwnerViewDog;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    private static final String REGEX_STRONG_PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,64}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonView(OwnerViewDog.class)
    protected Long id;

    @Column(nullable = false, unique = true, length = 150)
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email n'est pas au format valide")
    @JsonView(OwnerViewDog.class)
    protected String email;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le prénom ne peut pas être vide")
    @JsonView(OwnerViewDog.class)
    protected String firstname;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Le nom de famille ne peut pas être vide")
    @JsonView(OwnerViewDog.class)
    protected String lastname;

    @Column(nullable = false, length = 255) //taille de 255 caractères pour prévenir le mot de passe hashé
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
    @Pattern(regexp = REGEX_STRONG_PASSWORD, message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial")
    @JsonIgnore
    protected String password;

    @Column(nullable = true, length = 50)
    @JsonView(OwnerViewDog.class)
    protected String phone;

}
