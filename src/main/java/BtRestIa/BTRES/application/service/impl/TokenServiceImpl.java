package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.repository.UsuarioRepository;
import BtRestIa.BTRES.application.exception.UsuarioNotFoundException;
import BtRestIa.BTRES.application.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final UsuarioRepository usuarioRepository;

    public TokenServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario validateUsuarioToken(String token) {
        return usuarioRepository.findByToken(token)
                .orElseThrow(() -> new UsuarioNotFoundException(token));
    }

    @Override
    public String generateTokenForUsuario(Usuario usuario) {
        String newToken = UUID.randomUUID().toString();
        usuario.setToken(newToken);
        usuarioRepository.save(usuario);
        return newToken;
    }
}