package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.application.exception.ModeloAiNotFoundException;
import BtRestIa.BTRES.application.exception.PreguntaNotFoundException;
import BtRestIa.BTRES.application.exception.RespuestaNotFoundException;
import BtRestIa.BTRES.application.service.*;
import BtRestIa.BTRES.domain.*;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaServiceImpl implements ConsultaService {

    private final TokenService tokenService;
    private final GitService gitService;
    private final CodeChunkService chunkService;
    private final EmbeddingModel embeddingModel;
    private final OllamaChatModel.Builder modelBuilder;
    private final CodeEmbeddingRepository embeddingRepo;
    private final PreguntaRepository preguntaRepo;
    private final RespuestaRepository respuestaRepo;
    private final ConsultaRepository consultaRepo;
    private final ModeloIaRepository modeloIaRepository;

    private static final int TOP_K = 3;

    @Override
    @Transactional
    public RespuestaDto procesarPregunta(PreguntaRequestDto dto) throws GitAPIException, IOException {

        long startTotal = System.nanoTime();
        // 1. Validar usuario
        long t1 = System.nanoTime();
        Usuario usuario = tokenService.validateUsuarioToken(dto.getToken());
        log.info("Validación de usuario: {} ms", (System.nanoTime() - t1) / 1_000_000);

        // 2. Clonar repo
        long t2 = System.nanoTime();
        String repoName = UUID.randomUUID().toString();
        String repoPath = "repos/" + repoName;
        gitService.clonarRepo(dto.getRepoUrl(), repoPath);
        log.info("Clonación de repo: {} ms", (System.nanoTime() - t2) / 1_000_000);

        // 3. Chunking
        long t3 = System.nanoTime();
        List<String> fragments;
        fragments = chunkService.chunkCode(Paths.get(repoPath));
        log.info("Chunking: {} ms", (System.nanoTime() - t3) / 1_000_000);

        // 4. Generar embeddings y guardar fragments
        long t4 = System.nanoTime();
        for (int i = 0; i < fragments.size(); i++) {
            String frag = fragments.get(i);
            double[] vec = embedText(frag);
            String json = Arrays.toString(vec);
            embeddingRepo.save(CodeEmbedding.builder()
                    .repoName(repoName)
                    .fragment(frag)
                    .embeddingJson(json)
                    .build());
        }
        log.info("Embeddings y persistencia: {} ms", (System.nanoTime() - t4) / 1_000_000);

        // 5. Embedding de la pregunta
        long t5 = System.nanoTime();
        double[] queryVec = embedText(dto.getTexto());
        log.info("Embedding pregunta: {} ms", (System.nanoTime() - t5) / 1_000_000);

        // 6. Cargar embeddings y rankear
        long t6 = System.nanoTime();
        List<CodeEmbedding> topK = obtenerTopKFrag(queryVec, TOP_K);
        log.info("Ranking topK: {} ms", (System.nanoTime() - t6) / 1_000_000);
        
        // 7. Montar prompt

        StringBuilder prompt = new StringBuilder("Contexto de código:\n");
        topK.forEach(e -> prompt.append(e.getFragment()).append("\n---\n"));
        prompt.append("Pregunta: ").append(dto.getTexto());

        // 8. Generar respuesta con ChatClient y OllamaChatModel
        long t8 = System.nanoTime();
        OllamaOptions options = OllamaOptions.builder()
                .model(dto.getModelo())
                .temperature(0.3)
                .build();
        OllamaChatModel chatModel = modelBuilder
                .defaultOptions(options)
                .build();
        ChatResponse chatResponse = ChatClient.create(chatModel)
                .prompt(prompt.toString())
                .call()
                .chatResponse();
        String textoRespuesta = Optional.ofNullable(chatResponse)
                .map(ChatResponse::getResult)
                .map(r -> r.getOutput().getText())
                .orElseThrow(() -> new RespuestaNotFoundException("No se obtuvo respuesta"));
        log.info("Llamada a Ollama: {} ms", (System.nanoTime() - t8) / 1_000_000);

        // 9. Persistir Pregunta, Respuesta y Consulta

        Pregunta preg = preguntaRepo.save(
                Pregunta.builder()
                        .token(UUID.randomUUID().toString())
                        .texto(dto.getTexto())
                        .build()
        );

        Respuesta resp = respuestaRepo.save(
                Respuesta.builder()
                        .token(UUID.randomUUID().toString())
                        .texto(textoRespuesta)
                        .build()
        );
        // *Recuperar el ModeloIA* para la consulta
        ModeloIA modeloIA = modeloIaRepository.findByNombreAndActivoTrue(dto.getModelo())
                .orElseThrow(() -> new ModeloAiNotFoundException("Modelo no disponible: " + dto.getModelo()));

        consultaRepo.save(
                Consulta.builder()
                        .usuario(usuario)
                        .pregunta(preg)
                        .respuesta(resp)
                        .modeloIA(modeloIA)
                        .build()
        );
        log.info("Tiempo total procesarPregunta: {} ms", (System.nanoTime() - startTotal) / 1_000_000);
        // 10. Devolver DTO
        return RespuestaDto.of(resp.getToken(), resp.getTexto(), resp.getFecha());

    }


    // Obtiene embedding de un texto con EmbeddingModel
    private double[] embedText(String text) {
        EmbeddingResponse resp = embeddingModel.embedForResponse(List.of(text));
        Embedding emb = resp.getResults().get(0);
        float[] out = emb.getOutput();
        double[] vec = new double[out.length];
        for (int i = 0; i < out.length; i++) vec[i] = out[i];
        return vec;
    }

    // Distancia Euclídea
    private double euclidean(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double d = a[i] - b[i];
            sum += d * d;
        }
        return Math.sqrt(sum);
    }

    // Parse "[0.1, 0.2, ...]" a double[]
    private double[] parseJson(String json) {
        String[] parts = json.replaceAll("[\\[\\]\\s]", "").split(",");
        double[] v = new double[parts.length];
        for (int i = 0; i < parts.length; i++) v[i] = Double.parseDouble(parts[i]);
        return v;
    }

    // Obtener topK frang
    private List<CodeEmbedding> obtenerTopKFrag(double[] query, int topK) {
        List<CodeEmbedding> all = embeddingRepo.findAll();
        return all.stream()
                .map(e -> Map.entry(
                        e,
                        euclidean(parseJson(e.getEmbeddingJson()), query)
                ))
                .sorted(Map.Entry.comparingByValue())
                .limit(topK)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public PreguntaDto obtenerPreguntaPorToken(String token) {
        log.info("obtenerPreguntaPorToken: {}", token);
        return PreguntaDto.fromEntity(preguntaRepo.findByToken(token)
                .orElseThrow(() -> new PreguntaNotFoundException(token)));
    }

    @Override
    public RespuestaDto obtenerRespuestaPorToken(String token) {
        log.info("obtenerRespuestaPorToken: {}", token);
        return RespuestaDto.fromEntity(respuestaRepo.findByToken(token)
                .orElseThrow(() -> new RespuestaNotFoundException(token)));
    }
}