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
import org.springframework.web.server.ResponseStatusException;

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
import BtRestIa.BTRES.infrastructure.repository.ModeloIaRepository;
import BtRestIa.BTRES.infrastructure.repository.PreguntaRepository;
import BtRestIa.BTRES.infrastructure.repository.RespuestaRepository;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceImplTest {

    @Mock private TokenService tokenService;
    @Mock private PreguntaRepository preguntaRepo;
    @Mock private RespuestaRepository respuestaRepo;
    @Mock private ConsultaRepository consultaRepo;
    @Mock private ModeloIaRepository modeloRepo;
    @Mock private OllamaChatModel.Builder modelBuilder;
    @Mock private OllamaChatModel chatModel;

    @InjectMocks
    private ConsultaServiceImpl service;

    private final Usuario usuario = new Usuario();
    private final ModeloIA modeloIA = new ModeloIA(2L, "gpt", "chat", true);

    @Test
    void procesarPregunta_peticionValida_devuelveDtoYPersiste() {
        // Arrange
        try (MockedStatic<ChatClient> chatClientStatic = Mockito.mockStatic(ChatClient.class)) {
            ChatClient cliente = mock(ChatClient.class);
            ChatClientRequestSpec specPrompt = mock(ChatClientRequestSpec.class);
            CallResponseSpec specCall = mock(CallResponseSpec.class);
            ChatResponse fakeChatResponse = mock(ChatResponse.class);
            Generation fakeGen = mock(Generation.class);
            AssistantMessage fakeOutput = mock(AssistantMessage.class);

            chatClientStatic.when(() -> ChatClient.create(any(OllamaChatModel.class)))
                    .thenReturn(cliente);
            when(modelBuilder.build()).thenReturn(chatModel);
            when(cliente.prompt(anyString())).thenReturn(specPrompt);
            when(specPrompt.call()).thenReturn(specCall);
            when(specCall.chatResponse()).thenReturn(fakeChatResponse);
            when(fakeChatResponse.getResult()).thenReturn(fakeGen);
            when(fakeGen.getOutput()).thenReturn(fakeOutput);
            when(fakeOutput.getText()).thenReturn("Respuesta simulada");

            when(tokenService.validateUsuarioToken("token_user1")).thenReturn(usuario);
            when(modeloRepo.findByNombreAndActivoTrue("gpt")).thenReturn(Optional.of(modeloIA));
            when(preguntaRepo.save(any(Pregunta.class))).thenAnswer(inv -> inv.getArgument(0));
            when(respuestaRepo.save(any(Respuesta.class))).thenAnswer(inv -> inv.getArgument(0));
            when(consultaRepo.save(any(Consulta.class))).thenAnswer(inv -> inv.getArgument(0));

            // Act
            RespuestaDto resultado = service.procesarPregunta(
                    new PreguntaRequestDto("token_user1", "¿Cómo estás?", "gpt")
            );

            // Assert
            assertNotNull(resultado);
            assertEquals("Respuesta simulada", resultado.getTexto());
            verify(consultaRepo).save(any(Consulta.class));
        }
    }

}