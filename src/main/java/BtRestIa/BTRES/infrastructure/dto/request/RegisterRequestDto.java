package BtRestIa.BTRES.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message = "Nombre Obligatorio")
    private String nombre;

    @NotBlank(message = "Email Obligatorio")
    @Email(message = "Formato no valido")
    private String email;

    @NotBlank(message = "Password Obligatorio")
    private String password;

    private String role = "ROLE_USER";
}
