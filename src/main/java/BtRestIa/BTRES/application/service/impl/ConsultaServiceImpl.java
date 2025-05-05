package BtRestIa.BTRES.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import BtRestIa.BTRES.application.service.ConsultaService;
import BtRestIa.BTRES.application.service.TokenService;
import BtRestIa.BTRES.domain.*;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.repository.*;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultaServiceImpl implements ConsultaService {

        private final TokenService tokenService;
        private final PreguntaRepository preguntaRepository;
        private final RespuestaRepository respuestaRepository;
        private final ConsultaRepository consultaRepository;
        private final Modelo_iaRepository modeloIARepository;
        private final OllamaChatModel.Builder modelBuilder;


    @Override
    @Transactional
    public RespuestaDto procesarPregunta(PreguntaRequestDto dto) {

        // 1) validar token de usuario
        Usuario usuario = tokenService.validateUsuarioToken(dto.getToken());

        // 2) guardar pregunta
        Pregunta pregunta = preguntaRepository.save(
                Pregunta.builder()
                        .token(UUID.randomUUID().toString())
                        .texto(dto.getTexto())
                        .build()
        );

        // 3) verificar modelo IA
        ModeloIA modeloEntity = modeloIARepository.findByNombreAndActivoTrue(dto.getModelo())
                .orElseThrow(() -> new RuntimeException("Modelo IA no disponible"));

        // 4) construir y llamar a IA
        ChatResponse chatResponse = ChatClient.create(modelBuilder.build())
                .prompt(dto.getTexto())
                .call()
                .chatResponse();

        // 5) comprobaciones de nulidad
        String textoRespuesta = Objects.requireNonNull(
                Objects.requireNonNull(
                                Objects.requireNonNull(chatResponse, "La llamada a la IA devolvió chatResponse null")
                                        .getResult(), "getResult() es null")
                                        .getOutput(), "getOutput() es null").getText();

        // 6) guardar respuesta
        Respuesta respuesta = respuestaRepository.save(
                Respuesta.builder()
                        .token(UUID.randomUUID().toString())
                        .texto(textoRespuesta)
                        .build()
        );

        // 7) guardar consulta completa
        Consulta consulta = Consulta.builder()
                .usuario(usuario)
                .pregunta(pregunta)
                .respuesta(respuesta)
                .modeloIA(modeloEntity)
                .build();
        consultaRepository.save(consulta);

        // 8) devolver DTO
        return RespuestaDto.of(respuesta.getToken(), respuesta.getTexto(), respuesta.getFecha());
    }


        }

        @Override
        @Transactional(readOnly = true)
        public PreguntaDto obtenerPreguntaPorToken(String token) {
                Pregunta pregunta = preguntaRepository.findByToken(token)
                                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));
                return PreguntaDto.fromEntity(pregunta);
        }

        @Override
        @Transactional(readOnly = true)
        public RespuestaDto obtenerRespuestaPorToken(String token) {
                Respuesta respuesta = respuestaRepository.findByToken(token)
                                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));
                return RespuestaDto.fromEntity(respuesta);
        }

}

