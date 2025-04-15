package org.example.canicampusconnectapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class User {

    private static final String REGEX_STRONG_PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,64}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    @NotBlank(message = "Le prénom ne peut pas être vide")
    protected String firstname;

    @Column(nullable = false)
    @NotBlank(message = "Le nom de famille ne peut pas être vide")
    protected String lastname;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    @jakarta.validation.constraints.Email(message = "L'email n'est pas valide")
    protected String email;

    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
    @Pattern(regexp = REGEX_STRONG_PASSWORD, message = "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial")
    protected String password;

    @Column(nullable = true)
    protected String phone;

}
