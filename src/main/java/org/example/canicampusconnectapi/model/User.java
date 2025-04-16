package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

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
    protected Long id;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email n'est pas au format valide")
    protected String email;

    @Column(name = "firstname", nullable = false, length = 100)
    @NotBlank(message = "Le prénom ne peut pas être vide")
    protected String firstname;

    @Column(name = "lastname", nullable = false, length = 100)
    @NotBlank(message = "Le nom de famille ne peut pas être vide")
    protected String lastname;

    @Column(name = "password", nullable = false, length = 255) //taille de 255 caractères pour prévenir le mot de passe hashé
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
    @Pattern(regexp = REGEX_STRONG_PASSWORD, message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial")
    protected String password;

    @Column(name = "phone", nullable = true, length = 50)
    protected String phone;

}

