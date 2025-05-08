package BtRestIa.BTRES.application.mapper;

import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConsultaMapperTest {

    @Test
    void oPreguntaDto_consultaConPregunta_devuelveDtoCorrecto() {
        // Arrange
        Pregunta pregunta = Pregunta.builder()
                .id(1L)
                .token("pregunta-token")
                .texto("¿Cuál es la capital de Francia?")
                .fecha(LocalDateTime.now())
                .build();

        Consulta consulta = Consulta.builder()
                .pregunta(pregunta)
                .build();

        // Act
        PreguntaDto preguntaDto = ConsultaMapper.toPreguntaDto(consulta);

        // Assert
        assertNotNull(preguntaDto);
        assertEquals(pregunta.getToken(), preguntaDto.getToken());
        assertEquals(pregunta.getTexto(), preguntaDto.getTexto());
        assertEquals(pregunta.getFecha(), preguntaDto.getFecha());
    }

    @Test
    void toRespuestaDto_consultaConRespuesta_devuelveDtoCorrecto() {
        // Arrange
        Respuesta respuesta = Respuesta.builder()
                .id(1L)
                .token("respuesta-token")
                .texto("La capital de Francia es París.")
                .fecha(LocalDateTime.now())
                .build();

        Consulta consulta = Consulta.builder()
                .respuesta(respuesta)
                .build();

        // Act
        RespuestaDto respuestaDto = ConsultaMapper.toRespuestaDto(consulta);

        // Assert
        assertNotNull(respuestaDto);
        assertEquals(respuesta.getToken(), respuestaDto.getToken());
        assertEquals(respuesta.getTexto(), respuestaDto.getTexto());
        assertEquals(respuesta.getFecha(), respuestaDto.getFecha());
    }

    @Test
    void consultaMapper_sePuedeInstanciar_sinExcepciones() {
        // Act
        ConsultaMapper consultaMapper = new ConsultaMapper();

        // Assert
        assertNotNull(consultaMapper);
    }


}
