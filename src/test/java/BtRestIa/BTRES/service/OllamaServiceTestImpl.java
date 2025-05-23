package BtRestIa.BTRES.service;

import BtRestIa.BTRES.application.service.impl.OllamaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class OllamaServiceImplTest {

    @TestConfiguration
    @EnableRetry
    static class Config {
        @MockBean EmbeddingModel embeddingModel;
        @Bean
        OllamaServiceImpl ollamaServiceImpl(EmbeddingModel m) {
            return new OllamaServiceImpl(m);
        }
    }

    @Autowired
    private OllamaServiceImpl service;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Test
    void embed_shouldConvertFloatToDouble() {
        // Arrange
        float[] floats = {0.5f, 1.5f};
        Embedding mockEmb = mock(Embedding.class);
        when(mockEmb.getOutput()).thenReturn(floats);
        EmbeddingResponse mockResp = mock(EmbeddingResponse.class);
        when(mockResp.getResults()).thenReturn(List.of(mockEmb));
        when(embeddingModel.embedForResponse(List.of("hola"))).thenReturn(mockResp);

        // Act
        double[] result = service.embed("any-model", "hola");

        // Assert
        assertArrayEquals(new double[]{0.5, 1.5}, result, 1e-9);
        verify(embeddingModel).embedForResponse(List.of("hola"));
    }

    @Test
    void embed_shouldRetryUpTo3TimesOnException() {
        // Arrange: dos fallos y luego éxito
        float[] floats = {2f, 4f};
        Embedding mockEmb = mock(Embedding.class);
        when(mockEmb.getOutput()).thenReturn(floats);
        EmbeddingResponse okResp = mock(EmbeddingResponse.class);
        when(okResp.getResults()).thenReturn(Collections.singletonList(mockEmb));

        when(embeddingModel.embedForResponse(ArgumentMatchers.anyList()))
                .thenThrow(new RuntimeException("1"))
                .thenThrow(new RuntimeException("2"))
                .thenReturn(okResp);

        // Act
        double[] result = service.embed("retry-model", "texto");

        // Assert
        verify(embeddingModel, times(3)).embedForResponse(List.of("texto"));
        assertArrayEquals(new double[]{2.0, 4.0}, result, 1e-9);
    }
}