package BtRestIa.BTRES.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import BtRestIa.BTRES.application.constants.Constantes;
import BtRestIa.BTRES.application.exception.ModeloAiNotFoundException;
import BtRestIa.BTRES.application.exception.PreguntaNotFoundException;
import BtRestIa.BTRES.application.exception.RespuestaNotFoundException;
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
        private final ModeloIaRepository modeloIARepository;
        private final OllamaChatModel.Builder modelBuilder;

        @Override
        @Transactional
        public RespuestaDto procesarPregunta(PreguntaRequestDto dto) {
                // Validar usuario
                Usuario usuario = validarUsuario(dto.getToken());

                // Persistir pregunta
                Pregunta pregunta = guardarPregunta(dto.getTexto());

                // Obtener modelo IA
                ModeloIA modeloIA = obtenerModeloIA(dto.getModelo());

                // Generar respuesta desde IA
                String textoRespuesta = generarRespuestaIa(dto.getTexto());

                // Persistir respuesta
                Respuesta respuesta = guardarRespuesta(textoRespuesta);

                // Registrar consulta completa
                registrarConsulta(usuario, pregunta, respuesta, modeloIA);

                // Devolver DTO
                return mapearRespuestaDto(respuesta);
        }

        // Métodos privados auxiliares
        private Usuario validarUsuario(String token) {
                return tokenService.validateUsuarioToken(token);
        }

        private Pregunta guardarPregunta(String textoPregunta) {
                return preguntaRepository.save(
                                Pregunta.builder()
                                                .token(generarToken())
                                                .texto(textoPregunta)
                                                .build());
        }

        private ModeloIA obtenerModeloIA(String nombreModelo) {
                return modeloIARepository.findByNombreAndActivoTrue(nombreModelo)
                                .orElseThrow(() -> new ModeloAiNotFoundException(Constantes.MODELO_NO_DISPONIBLE));
        }

        private String generarRespuestaIa(String prompt) {
                ChatResponse response = ChatClient.create(modelBuilder.build())
                                .prompt(prompt)
                                .call()
                                .chatResponse();

                return extraerTextoRespuesta(response);
        }

        private String extraerTextoRespuesta(ChatResponse response) {
                return Objects.requireNonNull(
                                Objects.requireNonNull(
                                                Objects.requireNonNull(response, Constantes.CHAT_RESPONSE_NULL)
                                                                .getResult(),
                                                Constantes.CHAT_RESULT_NULL)
                                                .getOutput(),
                                Constantes.CHAT_OUTPUT_NULL).getText();
        }

        private Respuesta guardarRespuesta(String textoRespuesta) {
                return respuestaRepository.save(
                                Respuesta.builder()
                                                .token(generarToken())
                                                .texto(textoRespuesta)
                                                .build());
        }

        private void registrarConsulta(Usuario usuario, Pregunta pregunta, Respuesta respuesta, ModeloIA modelo) {
                consultaRepository.save(
                                Consulta.builder()
                                                .usuario(usuario)
                                                .pregunta(pregunta)
                                                .respuesta(respuesta)
                                                .modeloIA(modelo)
                                                .build());
        }

        private RespuestaDto mapearRespuestaDto(Respuesta respuesta) {
                return RespuestaDto.of(
                                respuesta.getToken(),
                                respuesta.getTexto(),
                                respuesta.getFecha());
        }

        private String generarToken() {
                return UUID.randomUUID().toString();
        }

        @Override
        public PreguntaDto obtenerPreguntaPorToken(String token) {
                return PreguntaDto.fromEntity(preguntaRepository.findByToken(token)
                                .orElseThrow(() -> new PreguntaNotFoundException(token)));
        }

        @Override
        public RespuestaDto obtenerRespuestaPorToken(String token) {
                return RespuestaDto.fromEntity(respuestaRepository.findByToken(token)
                                .orElseThrow(() -> new RespuestaNotFoundException(token)));
        }

}