package BtRestIa.BTRES.application.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import BtRestIa.BTRES.application.service.AuthUseCase;
import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.dto.request.LoginResquestDto;
import BtRestIa.BTRES.infrastructure.dto.response.JwtResponseDto;
import BtRestIa.BTRES.infrastructure.repository.UsuarioRepository;
import BtRestIa.BTRES.infrastructure.security.JwtProvider;

@Service
public class AuthServiceImpl implements AuthUseCase {

    private final UsuarioRepository usuarioRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, JwtProvider jwtProvider,
            BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtResponseDto login(LoginResquestDto loginRequestDto) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found with email: " + loginRequestDto.getEmail()));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new JwtResponseDto(jwtProvider.generateToken(usuario.getEmail(), usuario.getRole()));

    }

}
