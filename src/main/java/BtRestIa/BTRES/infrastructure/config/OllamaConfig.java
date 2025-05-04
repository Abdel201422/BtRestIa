package BtRestIa.BTRES.infrastructure.config;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.ollama.OllamaChatModel;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto; // Asegúrate de importar la clase correcta

@Configuration
public class OllamaConfig {

    @Bean
    public OllamaApi ollamaApi() {
        return new OllamaApi("http://localhost:11434");
    }

    @Bean
    public OllamaChatModel.Builder ollamaModelBuilder(OllamaApi ollamaApi) {
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaOptions.builder()
                                .model("llama3:8b")
                                .temperature(0.3)
                                // .format("json")
                                .build()
                );
    }
}
