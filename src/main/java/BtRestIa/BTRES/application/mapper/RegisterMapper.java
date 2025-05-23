package BtRestIa.BTRES.application.mapper;

import org.springframework.stereotype.Component;

import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.dto.request.RegisterRequestDto;

@Component
public class RegisterMapper {
    public Usuario toEntity(RegisterRequestDto registerDto, String encryptedPassword) {
        return Usuario.builder()
                .nombre(registerDto.getNombre())
                .email(registerDto.getEmail())
                .password(encryptedPassword)
                .role(registerDto.getRole() != null ? registerDto.getRole() : "ROLE_USER")
                .build();
    }
}
