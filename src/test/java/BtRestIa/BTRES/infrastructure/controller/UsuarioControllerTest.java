package BtRestIa.BTRES.infrastructure.controller;

import BtRestIa.BTRES.application.service.UsuarioService;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioServiceMock;

    @InjectMocks
    private UsuarioController usuarioController;

    private Pregunta pregunta1;
    private Pregunta pregunta2;
    private Respuesta respuesta1;
    private Respuesta respuesta2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pregunta1 = new Pregunta(1L, "token1", "¿Cuál es la capital de Francia?", null);
        pregunta2 = new Pregunta(2L, "token2", "¿Cuál es la capital de España?", null);

        respuesta1 = new Respuesta(1L, "token1", "La capital de Francia es París.", null);
        respuesta2 = new Respuesta(2L, "token2", "La capital de España es Madrid.", null);
    }

    @Test
    void getPreguntasUsuario_conTokenValido_devuelveLista() {
        String token = "user-token";
        List<Pregunta> preguntas = Arrays.asList(pregunta1, pregunta2);
        when(usuarioServiceMock.obtenerPreguntasPorUsuario(token)).thenReturn(preguntas);

        ResponseEntity<List<Pregunta>> response = usuarioController.getPreguntasUsuario(token);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(preguntas, response.getBody());
        verify(usuarioServiceMock, times(1)).obtenerPreguntasPorUsuario(token);
    }

    @Test
    void getRespuestasUsuario_conTokenValido_devuelveLista() {
        String token = "user-token";
        List<Respuesta> respuestas = Arrays.asList(respuesta1, respuesta2);
        when(usuarioServiceMock.obtenerRespuestasPorUsuario(token)).thenReturn(respuestas);

        ResponseEntity<List<Respuesta>> response = usuarioController.getRespuestasUsuario(token);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(respuestas, response.getBody());
        verify(usuarioServiceMock, times(1)).obtenerRespuestasPorUsuario(token);
    }
}
