package BtRestIa.BTRES.application.service.impl;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import BtRestIa.BTRES.application.service.ConsultaService;
import BtRestIa.BTRES.application.service.TokenService;
import BtRestIa.BTRES.domain.*;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.repository.*;

import java.util.Objects;
import java.util.UUID;

@Service
public class ConsultaServiceImpl implements ConsultaService {

    private final TokenService tokenService;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;
    private final ConsultaRepository consultaRepository;
    private final Modelo_iaRepository modeloIARepository;
    private final OllamaChatModel.Builder modelBuilder;

    public ConsultaServiceImpl(TokenService tokenService,
                               PreguntaRepository preguntaRepository,
                               RespuestaRepository respuestaRepository,
                               ConsultaRepository consultaRepository,
                               Modelo_iaRepository modeloIARepository,
                               OllamaChatModel.Builder modelBuilder) {
        this.tokenService = tokenService;
        this.preguntaRepository = preguntaRepository;
        this.respuestaRepository = respuestaRepository;
        this.consultaRepository = consultaRepository;
        this.modeloIARepository = modeloIARepository;
        this.modelBuilder = modelBuilder;
    }

    @Override
    @Transactional
    public RespuestaDto procesarPregunta(PreguntaRequestDto dto) {
        // 1) validar token de usuario
        Usuario usuario = tokenService.validateUsuarioToken(dto.getToken());

        // 2) guardar pregunta
        Pregunta pregunta = new Pregunta();
        pregunta.setToken(UUID.randomUUID().toString());
        pregunta.setTexto(dto.getTexto());
        pregunta = preguntaRepository.save(pregunta);

        // 3) verificar modelo IA
        ModeloIA modeloEntity = modeloIARepository
                .findByNombreAndActivoTrue(dto.getModelo())
                .orElseThrow(() -> new RuntimeException("Modelo IA no disponible"));

        // 4) construir y llamar a IA
        OllamaChatModel chatModel = modelBuilder.build();
        ChatClient chatClient = ChatClient.create(chatModel);

        ChatResponse chatResponse = chatClient
                .prompt(dto.getTexto())
                .call()
                .chatResponse();

        // 4.1) comprobaciones de nulidad
        Objects.requireNonNull(chatResponse, "La llamada a la IA devolvió chatResponse null");
        var result = Objects.requireNonNull(chatResponse.getResult(), "getResult() es null");
        var output = Objects.requireNonNull(result.getOutput(), "getOutput() es null");
        String textoRespuesta = Objects.requireNonNull(output.getText(), "getText() es null");

        // 5) guardar respuesta
        Respuesta respuesta = new Respuesta();
        respuesta.setToken(UUID.randomUUID().toString());
        respuesta.setTexto(textoRespuesta);
        respuesta = respuestaRepository.save(respuesta);

        // 6) guardar consulta completa
        Consulta consulta = new Consulta();
        consulta.setUsuario(usuario);
        consulta.setPregunta(pregunta);
        consulta.setRespuesta(respuesta);
        consulta.setModeloIA(modeloEntity);
        consultaRepository.save(consulta);

        // 7) devolver DTO
        return RespuestaDto.of(respuesta.getToken(), respuesta.getTexto(), respuesta.getFecha());
    }
}