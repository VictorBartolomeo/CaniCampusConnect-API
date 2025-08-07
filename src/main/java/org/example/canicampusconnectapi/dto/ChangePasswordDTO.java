package org.example.canicampusconnectapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.canicampusconnectapi.model.users.User;

@Getter
@Setter
public class ChangePasswordDTO {



    @NotBlank(message = "Le mot de passe actuel ne peut pas être vide",
            groups = {User.OnUpdatePassword.class})
    private String currentPassword;

    @NotBlank(message = "Le nouveau mot de passe ne peut pas être vide",
            groups = {User.OnUpdatePassword.class})
    @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,64}$",
            message = "Le mot de passe doit contenir au moins une majuscule, " +
                    "une minuscule, un chiffre et un caractère spécial")
    private String newPassword;
}