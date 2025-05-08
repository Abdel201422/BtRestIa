package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.application.service.UsuarioService;
import BtRestIa.BTRES.application.service.TokenService;
import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;
import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final TokenService tokenService;
    private final ConsultaRepository consultaRepository;

    public UsuarioServiceImpl(TokenService tokenService,
            ConsultaRepository consultaRepository) {
        this.tokenService = tokenService;
        this.consultaRepository = consultaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pregunta> obtenerPreguntasPorUsuario(String userToken) {
        Usuario usuario = tokenService.validateUsuarioToken(userToken);
        return consultaRepository.findByUsuario(usuario)
                .stream()
                .map(Consulta::getPregunta) // mapeo de Consulta → Pregunta
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Respuesta> obtenerRespuestasPorUsuario(String userToken) {
        Usuario usuario = tokenService.validateUsuarioToken(userToken);
        return consultaRepository.findByUsuario(usuario)
                .stream()
                .map(Consulta::getRespuesta) // mapeo de Consulta → Respuesta
                .toList();
    }
}