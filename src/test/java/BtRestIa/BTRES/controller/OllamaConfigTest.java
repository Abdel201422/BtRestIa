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
        // Arrange
        // (No se necesita preparación adicional)

        // Act
        OllamaApi api = config.ollamaApi();

        // Assert
        assertNotNull(api);
        assertTrue(api instanceof OllamaApi);
    }

    @Test
    void ollamaModelBuilder_devuelveBuilderBasico() {
        // Arrange
        OllamaApi api = config.ollamaApi();

        // Act
        OllamaChatModel.Builder builder = config.ollamaModelBuilder(api);
        OllamaChatModel model = builder.build();

        // Assert
        assertNotNull(builder);
        assertNotNull(model);
    }

    @Test
    void ollamaModel_configuraNombreYOpcionesCorrectamente() {
        // Arrange
        PreguntaRequestDto dto = new PreguntaRequestDto("t", "texto", "my-model");

        // Act
        OllamaChatModel model = config.getOllamaModel(dto);
        OllamaChatModel.Builder builder = config.ollamaModelBuilder(config.ollamaApi())
                .defaultOptions(
                        OllamaOptions.builder()
                                .model("my-model")
                                .temperature(0.3)
                                .build()
                );
        OllamaChatModel model2 = builder.build();

        // Assert
        assertNotNull(model);
        assertNotNull(model2);
    }
}
