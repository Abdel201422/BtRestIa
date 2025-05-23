package BtRestIa.BTRES.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import BtRestIa.BTRES.application.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import BtRestIa.BTRES.application.service.TokenService;
import BtRestIa.BTRES.domain.*;
import BtRestIa.BTRES.infrastructure.repository.ConsultaRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock TokenService tokenService;
    @Mock ConsultaRepository consultaRepo;
    @InjectMocks
    UsuarioServiceImpl service;

    @Test
    void obtenerPreguntasPorUsuario_tokenValido_devuelveLista() {
        // Arrange
        Usuario u = new Usuario();
        when(tokenService.validateUsuarioToken("tok")).thenReturn(u);

        Pregunta p1 = new Pregunta();
        Pregunta p2 = new Pregunta();
        when(consultaRepo.findByUsuario(u)).thenReturn(Arrays.asList(
                new Consulta(1L, u, null, null, p1),
                new Consulta(2L, u, null, null, p2)
        ));

        // Act
        List<Pregunta> preguntas = service.obtenerPreguntasPorUsuario("tok");

        // Assert
        assertEquals(2, preguntas.size());
        assertTrue(preguntas.containsAll(List.of(p1, p2)));
    }


    @Test
    void obtenerRespuestasPorUsuario_tokenValido_devuelveLista() {
        // Arrange
        Usuario u = new Usuario();
        when(tokenService.validateUsuarioToken("tok2")).thenReturn(u);

        Respuesta r1 = new Respuesta();
        Respuesta r2 = new Respuesta();
        when(consultaRepo.findByUsuario(u)).thenReturn(Arrays.asList(
                new Consulta(1L, u, null, r1, null),
                new Consulta(2L, u, null, r2, null)
        ));

        // Act
        List<Respuesta> respuestas = service.obtenerRespuestasPorUsuario("tok2");

        // Assert
        assertEquals(2, respuestas.size());
        assertTrue(respuestas.containsAll(List.of(r1, r2)));
    }

}