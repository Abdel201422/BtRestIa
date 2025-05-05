package BtRestIa.BTRES.infrastructure.config;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.ollama.OllamaChatModel;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;

@Configuration
public class OllamaConfig {

    @Bean
    public OllamaApi ollamaApi() {
        return new OllamaApi("http://localhost:11434");
    }

    @Bean
    public OllamaChatModel.Builder ollamaModelBuilder(OllamaApi ollamaApi) {
        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi);
    }
    // Para más detalles sobre las opciones de configuración, puedes consultar la documentación oficial de Ollama: https://github.com/ollama/ollama/blob/main/docs/api.md
    public OllamaChatModel getOllamaModel(PreguntaRequestDto preguntaRequestDto) {
        return ollamaModelBuilder(ollamaApi())
                .defaultOptions(
                        OllamaOptions.builder()
                                .model(preguntaRequestDto.getModelo())
                                .temperature(0.3)  // Temperature controla la aleatoriedad de las respuestas generadas, Valores más bajos hacen que las respuestas sean más deterministas.
                                .build()
                ).build();
    }
}
