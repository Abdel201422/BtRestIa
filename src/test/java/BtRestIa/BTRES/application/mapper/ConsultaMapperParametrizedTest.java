package BtRestIa.BTRES.application.mapper;

import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConsultaMapperParametrizedTest {

    // Fecha fija para evitar desalineaciones de nanosegundos
    private static final LocalDateTime FECHA_FIJA = LocalDateTime.of(
            2025, 5, 7, 16, 45, 26, 398_269_200
    );

    private static Stream<Arguments> provideConsultaForPreguntaDto() {
        return Stream.of(
                Arguments.of(
                        Consulta.builder()
                                .pregunta(Pregunta.builder()
                                        .id(1L)
                                        .token("pregunta-token-1")
                                        .texto("¿Cuál es la capital de Francia?")
                                        .fecha(FECHA_FIJA)
                                        .build())
                                .build(),
                        new PreguntaDto("pregunta-token-1", "¿Cuál es la capital de Francia?", FECHA_FIJA)
                ),
                Arguments.of(
                        Consulta.builder()
                                .pregunta(Pregunta.builder()
                                        .id(2L)
                                        .token("pregunta-token-2")
                                        .texto("¿Cuál es la capital de España?")
                                        .fecha(FECHA_FIJA)
                                        .build())
                                .build(),
                        new PreguntaDto("pregunta-token-2", "¿Cuál es la capital de España?", FECHA_FIJA)
                )
        );
    }

    private static Stream<Arguments> provideConsultaForRespuestaDto() {
        return Stream.of(
                Arguments.of(
                        Consulta.builder()
                                .respuesta(Respuesta.builder()
                                        .id(1L)
                                        .token("respuesta-token-1")
                                        .texto("La capital de Francia es París.")
                                        .fecha(FECHA_FIJA)
                                        .build())
                                .build(),
                        new RespuestaDto("respuesta-token-1", "La capital de Francia es París.", FECHA_FIJA)
                ),
                Arguments.of(
                        Consulta.builder()
                                .respuesta(Respuesta.builder()
                                        .id(2L)
                                        .token("respuesta-token-2")
                                        .texto("La capital de España es Madrid.")
                                        .fecha(FECHA_FIJA)
                                        .build())
                                .build(),
                        new RespuestaDto("respuesta-token-2", "La capital de España es Madrid.", FECHA_FIJA)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideConsultaForPreguntaDto")
    @DisplayName("Convertir Consulta a PreguntaDto")
    void toPreguntaDto_consultaConPregunta_devuelveDtoCorrecto(Consulta consulta, PreguntaDto expected) {
        // Act
        PreguntaDto preguntaDto = ConsultaMapper.toPreguntaDto(consulta);

        // Assert
        assertNotNull(preguntaDto);
        assertEquals(expected.getToken(), preguntaDto.getToken());
        assertEquals(expected.getTexto(), preguntaDto.getTexto());
        assertEquals(expected.getFecha(), preguntaDto.getFecha());
    }

    @ParameterizedTest
    @MethodSource("provideConsultaForRespuestaDto")
    @DisplayName("Convertir Consulta a RespuestaDto")
    void toRespuestaDto_consultaConRespuesta_devuelveDtoCorrecto(Consulta consulta, RespuestaDto expected) {
        // Act
        RespuestaDto respuestaDto = ConsultaMapper.toRespuestaDto(consulta);

        // Assert
        assertNotNull(respuestaDto);
        assertEquals(expected.getToken(), respuestaDto.getToken());
        assertEquals(expected.getTexto(), respuestaDto.getTexto());
        assertEquals(expected.getFecha(), respuestaDto.getFecha());
    }
}