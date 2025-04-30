package BtRestIa.BTRES.infrastructure.controller;

import BtRestIa.BTRES.application.service.UsuarioService;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{token}/preguntas")
    public ResponseEntity<List<Pregunta>> getPreguntasUsuario(@PathVariable String token) {
        List<Pregunta> preguntas = usuarioService.obtenerPreguntasPorUsuario(token);
        return ResponseEntity.ok(preguntas);
    }

    @GetMapping("/{token}/respuestas")
    public ResponseEntity<List<Respuesta>> getRespuestasUsuario(@PathVariable String token) {
        List<Respuesta> respuestas = usuarioService.obtenerRespuestasPorUsuario(token);
        return ResponseEntity.ok(respuestas);
    }
}