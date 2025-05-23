package BtRestIa.BTRES.infrastructure.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginResquestDto {

    @NotBlank(message = "Email Obligatorio")
    @Email(message = "Formato no valido")
    private String email;

    @NotBlank(message = "Password Obligatorio")
    private String password;

}
