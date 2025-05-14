package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.application.exception.PreguntaNotFoundException;
import BtRestIa.BTRES.application.exception.RespuestaNotFoundException;
import BtRestIa.BTRES.application.service.*;
import BtRestIa.BTRES.domain.CodeEmbedding;
import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;
import BtRestIa.BTRES.domain.Usuario;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
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
public class ConsultaServiceImpl implements ConsultaService {

        private final TokenService tokenService;
        private final GitService gitService;
        private final CodeChunkService chunkService;
        private final EmbeddingModel embeddingModel;            // para embeddings
        private final OllamaChatModel.Builder modelBuilder;     // tu OllamaConfig
        private final CodeEmbeddingRepository embeddingRepo;
        private final PreguntaRepository preguntaRepo;
        private final RespuestaRepository respuestaRepo;
        private final ConsultaRepository consultaRepo;

        private static final int TOP_K = 5;

        @Override
        @Transactional
        public RespuestaDto procesarPregunta(PreguntaRequestDto dto) throws GitAPIException {
                // 1. Validar usuario
                Usuario usuario = tokenService.validateUsuarioToken(dto.getToken());

                // 2. Clonar repo
                String repoName = UUID.randomUUID().toString();
                String repoPath = "repos/" + repoName;
                gitService.clonarRepo(dto.getRepoUrl(), repoPath);

                // 3. Chunking
                List<String> fragments;
                try {
                        fragments = chunkService.chunkCode(Paths.get(repoPath));
                } catch (IOException e) {
                        throw new RuntimeException("Error al chunkear el código", e);
                }

                // 4. Generar embeddings y guardar fragments
                for (String frag : fragments) {
                        double[] vec = embedText(frag);
                        String json = Arrays.toString(vec);
                        embeddingRepo.save(CodeEmbedding.builder()
                                .repoName(repoName)
                                .fragment(frag)
                                .embeddingJson(json)
                                .build());
                }

                // 5. Embedding de la pregunta
                double[] queryVec = embedText(dto.getTexto());

                // 6. Cargar embeddings y rankear
                List<CodeEmbedding> all = embeddingRepo.findAll();
                List<CodeEmbedding> topK = all.stream()
                        .map(e -> Map.entry(
                                e,
                                euclidean(parseJson(e.getEmbeddingJson()), queryVec)
                        ))
                        .sorted(Map.Entry.comparingByValue())
                        .limit(TOP_K)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                // 7. Montar prompt
                StringBuilder prompt = new StringBuilder("Contexto de código:\n");
                topK.forEach(e -> prompt.append(e.getFragment()).append("\n---\n"));
                prompt.append("Pregunta: ").append(dto.getTexto());

                // 8. Generar respuesta con ChatClient y OllamaChatModel
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
                consultaRepo.save(
                        Consulta.builder()
                                .usuario(usuario)
                                .pregunta(preg)
                                .respuesta(resp)
                                .build()
                );

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

        @Override
        public PreguntaDto obtenerPreguntaPorToken(String token) {
                return PreguntaDto.fromEntity(preguntaRepo.findByToken(token)
                        .orElseThrow(() -> new PreguntaNotFoundException(token)));
        }

        @Override
        public RespuestaDto obtenerRespuestaPorToken(String token) {
                return RespuestaDto.fromEntity(respuestaRepo.findByToken(token)
                        .orElseThrow(() -> new RespuestaNotFoundException(token)));
        }
}