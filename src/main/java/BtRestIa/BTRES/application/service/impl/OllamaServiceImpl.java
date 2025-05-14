package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.application.service.OllamaService;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OllamaServiceImpl implements OllamaService {

    private final EmbeddingModel embeddingModel;

    public OllamaServiceImpl(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public double[] embed(String modelName, String text) {
        // 1. Llamamos al modelo de embeddings (se inyecta un EmbeddingModel configurado para Ollama)
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(text));  // :contentReference[oaicite:0]{index=0}

        // 2. Extraemos la lista de Embedding
        List<Embedding> results = response.getResults();                              // :contentReference[oaicite:1]{index=1}

        // 3. Del primer Embedding sacamos el vector (float[]) y lo convertimos a double[]
        float[] vectorFloat = results.get(0).getOutput();                             // :contentReference[oaicite:2]{index=2}
        double[] vector = new double[vectorFloat.length];
        for (int i = 0; i < vectorFloat.length; i++) {
            vector[i] = vectorFloat[i];
        }

        return vector;
    }
}