package BtRestIa.BTRES.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.ollama.OllamaChatModel;

import BtRestIa.BTRES.application.service.impl.ConsultaServiceImpl;
import BtRestIa.BTRES.application.service.TokenService;
import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;
import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.domain.ModeloIA;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.repository.ConsultaRepository;
import BtRestIa.BTRES.infrastructure.repository.Modelo_iaRepository;
import BtRestIa.BTRES.infrastructure.repository.PreguntaRepository;
import BtRestIa.BTRES.infrastructure.repository.RespuestaRepository;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceImplTest {

    @Mock private TokenService tokenService;
    @Mock private PreguntaRepository preguntaRepo;
    @Mock private RespuestaRepository respuestaRepo;
    @Mock private ConsultaRepository consultaRepo;
    @Mock private Modelo_iaRepository modeloRepo;
    @Mock private OllamaChatModel.Builder modelBuilder;
    @Mock private OllamaChatModel chatModel;

    @InjectMocks
    private ConsultaServiceImpl service;

    private final Usuario usuario = new Usuario();
    private final ModeloIA modeloIA = new ModeloIA(2L, "gpt", "chat", true);

    @Test
    void procesarPregunta_flujoCompleto_devuelveDtoYPersisteEntidades() {
        try (MockedStatic<ChatClient> chatClientStatic = Mockito.mockStatic(ChatClient.class)) {
            // Mock del cliente de chat y llamadas encadenadas
            ChatClient cliente = mock(ChatClient.class);
            ChatClientRequestSpec specPrompt = mock(ChatClientRequestSpec.class);
            CallResponseSpec specCall = mock(CallResponseSpec.class);
            ChatResponse fakeChatResponse = mock(ChatResponse.class);
            Generation fakeGen = mock(Generation.class);
            AssistantMessage fakeOutput = mock(AssistantMessage.class);

            // Stub estático de creación de cliente
            chatClientStatic.when(() -> ChatClient.create(any(OllamaChatModel.class)))
                    .thenReturn(cliente);
            // Stub del builder de modelo
            when(modelBuilder.build()).thenReturn(chatModel);
            // Flujo fluido completo
            when(cliente.prompt(anyString())).thenReturn(specPrompt);
            when(specPrompt.call()).thenReturn(specCall);
            when(specCall.chatResponse()).thenReturn(fakeChatResponse);
            when(fakeChatResponse.getResult()).thenReturn(fakeGen);
            when(fakeGen.getOutput()).thenReturn(fakeOutput);
            when(fakeOutput.getText()).thenReturn("Respuesta simulada");

            // Mocks de dependencias
            when(tokenService.validateUsuarioToken("token_user1")).thenReturn(usuario);
            when(modeloRepo.findByNombreAndActivoTrue("gpt")).thenReturn(Optional.of(modeloIA));
            when(preguntaRepo.save(any(Pregunta.class))).thenAnswer(inv -> inv.getArgument(0));
            when(respuestaRepo.save(any(Respuesta.class))).thenAnswer(inv -> inv.getArgument(0));
            when(consultaRepo.save(any(Consulta.class))).thenAnswer(inv -> inv.getArgument(0));

            // Ejecutar
            RespuestaDto resultado = service.procesarPregunta(
                    new PreguntaRequestDto("token_user1", "¿Cómo estás?", "gpt")
            );

            // Verificar
            assertNotNull(resultado);
            assertEquals("Respuesta simulada", resultado.getTexto());
            verify(consultaRepo).save(any(Consulta.class));
        }
    }

    @Test
    void procesarPregunta_responseNull_lanzaNullPointerException() {
        try (MockedStatic<ChatClient> chatClientStatic = Mockito.mockStatic(ChatClient.class)) {
            ChatClient cliente = mock(ChatClient.class);
            ChatClientRequestSpec specPrompt = mock(ChatClientRequestSpec.class);
            CallResponseSpec specCall = mock(CallResponseSpec.class);

            chatClientStatic.when(() -> ChatClient.create(any(OllamaChatModel.class)))
                    .thenReturn(cliente);
            when(modelBuilder.build()).thenReturn(chatModel);
            when(cliente.prompt(anyString())).thenReturn(specPrompt);
            when(specPrompt.call()).thenReturn(specCall);
            // Forzar chatResponse null
            when(specCall.chatResponse()).thenReturn(null);

            when(tokenService.validateUsuarioToken(anyString())).thenReturn(usuario);
            when(modeloRepo.findByNombreAndActivoTrue("gpt")).thenReturn(Optional.of(modeloIA));

            NullPointerException ex = assertThrows(NullPointerException.class,
                    () -> service.procesarPregunta(
                            new PreguntaRequestDto("t", "¿Hola?", "gpt")
                    )
            );
            assertEquals("La llamada a la IA devolvió chatResponse null", ex.getMessage());
        }
    }

    @Test
    void procesarPregunta_modeloNoEncontrado_lanzaRuntimeException() {
        when(tokenService.validateUsuarioToken(anyString())).thenReturn(usuario);
        when(modeloRepo.findByNombreAndActivoTrue("inexistente")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.procesarPregunta(
                        new PreguntaRequestDto("x", "¿Hola?", "inexistente")
                )
        );
        assertEquals("Modelo IA no disponible", ex.getMessage());
    }

    @Test
    void obtenerPreguntaPorToken_existente_devuelveDto() {
        // Preparar mock
        Pregunta pregunta = new Pregunta();
        pregunta.setToken("token-p");
        pregunta.setTexto("Texto pregunta");
        when(preguntaRepo.findByToken("token-p")).thenReturn(Optional.of(pregunta));

        // Ejecutar
        PreguntaDto dto = service.obtenerPreguntaPorToken("token-p");

        // Verificar
        assertNotNull(dto);
        assertEquals("token-p", dto.getToken());
        assertEquals("Texto pregunta", dto.getTexto());
    }

    @Test
    void obtenerPreguntaPorToken_inexistente_lanzaRuntimeException() {
        when(preguntaRepo.findByToken("nope")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.obtenerPreguntaPorToken("nope")
        );
        assertEquals("Pregunta no encontrada", ex.getMessage());
    }

    @Test
    void obtenerRespuestaPorToken_existente_devuelveDto() {
        // Preparar mock
        Respuesta respuesta = new Respuesta();
        respuesta.setToken("token-r");
        respuesta.setTexto("Texto respuesta");
        when(respuestaRepo.findByToken("token-r")).thenReturn(Optional.of(respuesta));

        // Ejecutar
        RespuestaDto dto = service.obtenerRespuestaPorToken("token-r");

        // Verificar
        assertNotNull(dto);
        assertEquals("token-r", dto.getToken());
        assertEquals("Texto respuesta", dto.getTexto());
    }

    @Test
    void obtenerRespuestaPorToken_inexistente_lanzaRuntimeException() {
        when(respuestaRepo.findByToken("nope")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.obtenerRespuestaPorToken("nope")
        );
        assertEquals("Respuesta no encontrada", ex.getMessage());
    }
}