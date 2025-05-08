package BtRestIa.BTRES.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import BtRestIa.BTRES.domain.ModeloIA;
import BtRestIa.BTRES.infrastructure.repository.ModeloIaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class ModeloIaControllerTest {

    @Mock
    private ModeloIaRepository modeloIaRepositoryMock;

    @InjectMocks
    private ModeloIaController modeloIaController;

    private ModeloIA modelo1;
    private ModeloIA modelo2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        modelo1 = new ModeloIA(1L, "Mistral:latest", "Modelo de IA Mistral", true);
        modelo2 = new ModeloIA(2L, "llama3:8b", "Modelo de IA Llama", true);
    }

    @Test
    void getModeloIaRepository_devuelveListaDeModelos() {
        List<ModeloIA> modelos = Arrays.asList(modelo1, modelo2);
        when(modeloIaRepositoryMock.findAll()).thenReturn(modelos);

        List<ModeloIA> response = modeloIaController.getModeloIaRepository();

        assertEquals(2, response.size());
        assertEquals(modelo1, response.get(0));
        assertEquals(modelo2, response.get(1));
    }
}
