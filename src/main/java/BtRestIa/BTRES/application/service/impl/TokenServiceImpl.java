package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.repository.UsuarioRepository;
import BtRestIa.BTRES.infrastructure.repository.UsuarioRepository;
import BtRestIa.BTRES.application.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario validateUsuarioToken(String token) {
        return usuarioRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con token: " + token
                ));
    }


    @Override
    public String generateTokenForUsuario(Usuario usuario) {
        String newToken = UUID.randomUUID().toString();
        usuario.setToken(newToken);
        usuarioRepository.save(usuario);
        return newToken;
    }
}