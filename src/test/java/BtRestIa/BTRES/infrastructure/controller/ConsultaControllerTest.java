package BtRestIa.BTRES.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        preguntaRequestDto = new PreguntaRequestDto("token_user1", "¿Cuál es la capital de Francia?", "mistral:latest");
        respuestaDto = new RespuestaDto("response-token", "La capital de Francia es París.", null);
        preguntaDto = new PreguntaDto("question-token", "¿Cuál es la capital de Francia?", null);
    }

    @Test
    void preguntar_conPeticionValida_devuelveRespuestaDto() {
        // Arrange
        when(consultaServiceMock.procesarPregunta(preguntaRequestDto)).thenReturn(respuestaDto);

        // Act
        ResponseEntity<RespuestaDto> response = consultaController.preguntar(preguntaRequestDto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(respuestaDto, response.getBody());
        verify(consultaServiceMock, times(1)).procesarPregunta(preguntaRequestDto);
    }

    @Test
    void obtenerRespuestaPorToken_conTokenValido_devuelveRespuestaDto() {
        // Arrange
        String token = "response-token";
        when(consultaServiceMock.obtenerRespuestaPorToken(token)).thenReturn(respuestaDto);

        // Act
        ResponseEntity<RespuestaDto> response = consultaController.obtenerRespuestaPorToken(token);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(respuestaDto, response.getBody());
        verify(consultaServiceMock, times(1)).obtenerRespuestaPorToken(token);
    }

    @Test
    void obtenerPreguntaPorToken_conTokenValido_devuelvePreguntaDto() {
        // Arrange
        String token = "question-token";
        when(consultaServiceMock.obtenerPreguntaPorToken(token)).thenReturn(preguntaDto);

        // Act
        ResponseEntity<PreguntaDto> response = consultaController.obtenerPreguntaPorToken(token);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(preguntaDto, response.getBody());
        verify(consultaServiceMock, times(1)).obtenerPreguntaPorToken(token);
    }
}
