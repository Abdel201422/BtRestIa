package BtRestIa.BTRES.service;

import BtRestIa.BTRES.application.exception.ModeloAiNotFoundException;
import BtRestIa.BTRES.application.exception.RespuestaNotFoundException;
import BtRestIa.BTRES.application.service.GitService;
import BtRestIa.BTRES.application.service.CodeChunkService;
import BtRestIa.BTRES.application.service.TokenService;
import BtRestIa.BTRES.application.service.impl.ConsultaServiceImpl;
import BtRestIa.BTRES.domain.*;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.repository.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceImplFullFlowTest {

    @Mock TokenService tokenService;
    @Mock GitService gitService;
    @Mock CodeChunkService chunkService;
    @Mock EmbeddingModel embeddingModel;
    @Mock OllamaChatModel.Builder modelBuilder;
    @Mock CodeEmbeddingRepository embeddingRepo;
    @Mock PreguntaRepository preguntaRepo;
    @Mock RespuestaRepository respuestaRepo;
    @Mock ConsultaRepository consultaRepo;
    @Mock ModeloIaRepository modeloIaRepository;

    @InjectMocks
    private ConsultaServiceImpl service;


    @Test
    void procesarPregunta_fullFlow_happyPath() throws GitAPIException, IOException {
        // === Arrange ===
        var dto = new PreguntaRequestDto(
                "token-xyz",
                "¿Cómo funciona?",
                "mi-modelo",
                "https://github.com/usuario/repo"
        );
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        // 1) Validar token
        when(tokenService.validateUsuarioToken("token-xyz")).thenReturn(usuario);

        // 2) Clonar repo (sin excepción)
        doNothing().when(gitService).clonarRepo(anyString(), anyString());

        // 3) Chunking → 2 fragmentos
        List<String> frags = List.of("código A", "código B");
        when(chunkService.chunkCode(Paths.get("repos/" + anyString())))
                .thenReturn(frags);

        // 4) Embeddings de fragments
        float[] embFloats = new float[]{0.1f, 0.2f};
        Embedding mockEmb = mock(Embedding.class);
        when(mockEmb.getOutput()).thenReturn(embFloats);
        EmbeddingResponse resp = mock(EmbeddingResponse.class);
        when(resp.getResults()).thenReturn(List.of(mockEmb));
        when(embeddingModel.embedForResponse(List.of("código A"))).thenReturn(resp);
        when(embeddingModel.embedForResponse(List.of("código B"))).thenReturn(resp);
        // Cada save devuelve la misma entidad
        when(embeddingRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // 5) Carga saved embeddings para rankeo
        CodeEmbedding e1 = CodeEmbedding.builder()
                .fragment("código A")
                .embeddingJson("[0.1,0.2]").build();
        CodeEmbedding e2 = CodeEmbedding.builder()
                .fragment("código B")
                .embeddingJson("[0.1,0.2]").build();
        when(embeddingRepo.findAll()).thenReturn(List.of(e1, e2));

        // 6) Embedding de la pregunta
        when(embeddingModel.embedForResponse(List.of("¿Cómo funciona?")))
                .thenReturn(resp);

        // 7) Montar y llamar a ChatClient
        AssistantMessage outMsg = mock(AssistantMessage.class);
        Generation gen = mock(Generation.class);
        when(gen.getOutput()).thenReturn(outMsg);
        when(outMsg.getText()).thenReturn("Respuesta IA");
        ChatResponse chatResponse = mock(ChatResponse.class);
        when(chatResponse.getResult()).thenReturn(gen);

        ChatClient cliente = mock(ChatClient.class);
        ChatClientRequestSpec specPrompt = mock(ChatClientRequestSpec.class);
        CallResponseSpec specCall = mock(CallResponseSpec.class);

        try (MockedStatic<ChatClient> chatStatic = mockStatic(ChatClient.class)) {
            chatStatic.when(() -> ChatClient.create(any(OllamaChatModel.class)))
                    .thenReturn(cliente);
            when(cliente.prompt(anyString())).thenReturn(specPrompt);
            when(specPrompt.call()).thenReturn(specCall);
            when(specCall.chatResponse()).thenReturn(chatResponse);

            // 8) Modelo IA disponible
            ModeloIA modelo = new ModeloIA(5L, "mi-modelo", "chat", true);
            when(modeloIaRepository.findByNombreAndActivoTrue("mi-modelo"))
                    .thenReturn(Optional.of(modelo));

            // 9) Guardar Pregunta y Respuesta
            when(preguntaRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(respuestaRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(consultaRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            // === Act ===
            RespuestaDto result = service.procesarPregunta(dto);

            // === Assert ===
            assertThat(result).isNotNull();
            assertThat(result.getTexto()).isEqualTo("Respuesta IA");
            // Verificar que se guardaron todos los fragments
            verify(embeddingRepo, times(2)).save(any(CodeEmbedding.class));
            // Verificar que se llamó a ChatClient con el prompt que incluye ambos fragments
            verify(cliente).prompt(contains("código A"));
            verify(cliente).prompt(contains("código B"));
            // Verificar persistencia de entidad final
            verify(consultaRepo).save(any());
        }
    }

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