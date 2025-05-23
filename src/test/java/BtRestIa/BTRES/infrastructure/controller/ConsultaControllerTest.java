package BtRestIa.BTRES.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import BtRestIa.BTRES.application.service.ConsultaService;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;

import java.io.IOException;

class ConsultaControllerTest {

    @Mock
    private ConsultaService consultaServiceMock;

    @InjectMocks
    private ConsultaController consultaController;

    private PreguntaRequestDto preguntaRequestDto;
    private RespuestaDto respuestaDto;
    private PreguntaDto preguntaDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        preguntaRequestDto = new PreguntaRequestDto("token_user1", "¿Cuál es la capital de Francia?", "mistral:latest","https://github.com/usuario/repositorio");
        respuestaDto = new RespuestaDto("response-token", "La capital de Francia es París.", null);
        preguntaDto = new PreguntaDto("question-token", "¿Cuál es la capital de Francia?", null);
    }

    @Test
    void preguntar_conPeticionValida_devuelveRespuestaDto() throws GitAPIException, IOException {
        when(consultaServiceMock.procesarPregunta(preguntaRequestDto)).thenReturn(respuestaDto);

        ResponseEntity<RespuestaDto> response = consultaController.preguntar(preguntaRequestDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(respuestaDto, response.getBody());
        verify(consultaServiceMock, times(1)).procesarPregunta(preguntaRequestDto);
    }

    @Test
    void obtenerRespuestaPorToken_conTokenValido_devuelveRespuestaDto() {
        String token = "response-token";
        when(consultaServiceMock.obtenerRespuestaPorToken(token)).thenReturn(respuestaDto);

        ResponseEntity<RespuestaDto> response = consultaController.obtenerRespuestaPorToken(token);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(respuestaDto, response.getBody());
        verify(consultaServiceMock, times(1)).obtenerRespuestaPorToken(token);
    }

    @Test
    void obtenerPreguntaPorToken_conTokenValido_devuelvePreguntaDto() {
        String token = "question-token";
        when(consultaServiceMock.obtenerPreguntaPorToken(token)).thenReturn(preguntaDto);

        ResponseEntity<PreguntaDto> response = consultaController.obtenerPreguntaPorToken(token);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(preguntaDto, response.getBody());
        verify(consultaServiceMock, times(1)).obtenerPreguntaPorToken(token);
    }
}
