package org.example.canicampusconnectapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email n'est pas au format valide")
    private String email;

    @NotBlank(message = "Veuillez renseigner un mot de passe")
    private String password;

}
