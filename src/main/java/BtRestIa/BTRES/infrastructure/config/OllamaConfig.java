package BtRestIa.BTRES.infrastructure.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.ollama.OllamaChatModel;

@Configuration
public class OllamaConfig {

    /**
     * Bean que carga el host de application.properties,
     * pero NO fija el modelo, así podemos elegirlo en cada llamada.
     */
    @Bean
    public OllamaChatModel.Builder ollamaModelBuilder() {
        return OllamaChatModel.builder();
    }
}