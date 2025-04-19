package BtRestIa.BTRES.infrastructure.controller;

import BtRestIa.BTRES.application.mapper.ConsultaMapper;

import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.repository.ConsultaRepository;
import BtRestIa.BTRES.application.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuario")
public class usuarioController {
    private final TokenService tokenService;
    private final ConsultaRepository consultaRepository;

    @Autowired
    public usuarioController(TokenService tokenService,
                             ConsultaRepository consultaRepository) {
        this.tokenService = tokenService;
        this.consultaRepository = consultaRepository;
    }

    @GetMapping("/{token}/pregunta")
    public ResponseEntity<List<PreguntaDto>> getPreguntas(@PathVariable String token) {
        Usuario usuario = tokenService.validateUsuarioToken(token);
        List<Consulta> consultas = consultaRepository.findByUsuario(usuario);
        List<PreguntaDto> preguntas = consultas.stream()
                .map(consulta -> ConsultaMapper.toPreguntaDto(consulta))
                .collect(Collectors.toList());
        return ResponseEntity.ok(preguntas);
    }

    @GetMapping("/{token}/respuesta")
    public ResponseEntity<List<RespuestaDto>> getRespuestas(@PathVariable String token) {
        Usuario usuario = tokenService.validateUsuarioToken(token);
        List<Consulta> consultas = consultaRepository.findByUsuario(usuario);
        List<RespuestaDto> respuestas = consultas.stream()
                .map(consulta -> ConsultaMapper.toRespuestaDto(consulta))
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }
}