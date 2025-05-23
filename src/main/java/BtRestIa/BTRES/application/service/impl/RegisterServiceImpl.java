package BtRestIa.BTRES.application.service.impl;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import BtRestIa.BTRES.application.mapper.RegisterMapper;
import BtRestIa.BTRES.application.service.RegisterUseCase;
import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.dto.request.RegisterRequestDto;
import BtRestIa.BTRES.infrastructure.repository.UsuarioRepository;

@Service
public class RegisterServiceImpl implements RegisterUseCase {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegisterMapper registerMapper;

    public RegisterServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder, RegisterMapper registerMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.registerMapper = registerMapper;
    }

    @Override
    public void register(RegisterRequestDto registerDto) {
      Optional<Usuario> existingEmail = usuarioRepository.findByEmail(registerDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("User with email " + registerDto.getEmail() + " already exists.");
        }

        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
        usuarioRepository.save(registerMapper.toEntity(registerDto, encryptedPassword));
    }
    
}
