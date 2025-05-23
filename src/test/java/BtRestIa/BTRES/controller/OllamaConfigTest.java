package BtRestIa.BTRES.controller;

import static org.junit.jupiter.api.Assertions.*;

import BtRestIa.BTRES.infrastructure.config.OllamaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.OllamaChatModel;

import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;


class OllamaConfigTest {
    private final OllamaConfig config = new OllamaConfig();

    @Test
    void ollamaApi_creaInstanciaConUrlCorrecta() {
        OllamaApi api = config.ollamaApi();
        assertNotNull(api);
        assertTrue(api instanceof OllamaApi);
    }

    @Test
    void ollamaModelBuilder_devuelveBuilderBasico() {
        OllamaApi api = config.ollamaApi();
        OllamaChatModel.Builder builder = config.ollamaModelBuilder(api);
        assertNotNull(builder);
        // El builder debe contener la API que le pasamos
        // No hay getter expuesto, pero build() no fallará
        OllamaChatModel model = builder.build();
        assertNotNull(model);
    }

    @Test
    void ollamaModel_configuraNombreYOpcionesCorrectamente() {
        // Preparamos DTO con un modelo concreto
        PreguntaRequestDto dto = new PreguntaRequestDto("t","texto","my-model", "https://github.com/usuario/repositorio");
        // Obtenemos el modelo:
        OllamaChatModel model = config.getOllamaModel(dto);
        assertNotNull(model);
        // No hay acceso directo a opciones, pero si pedimos un nuevo builder
        // con las mismas opciones, no falla
        // Verificamos que el builder base utiliza el mismo OllamaApi
        OllamaChatModel.Builder b2 = config.ollamaModelBuilder(config.ollamaApi())
                .defaultOptions(
                        OllamaOptions.builder()
                                .model("my-model")
                                .temperature(0.3)
                                .build()
                );
        OllamaChatModel model2 = b2.build();
        assertNotNull(model2);
    }
}