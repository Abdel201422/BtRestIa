package BtRestIa.BTRES.infrastructure.controller;

import BtRestIa.BTRES.domain.ModeloIA;
import BtRestIa.BTRES.infrastructure.repository.ModeloIaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class ModeloIaController {

    private ModeloIaRepository modeloIaRepository;

    public ModeloIaController(ModeloIaRepository modeloIaRepository) {
        this.modeloIaRepository = modeloIaRepository;
    }

    @GetMapping("/api/modelo_ia/modelo")
    public List<ModeloIA> getModeloIaRepository() {
        return modeloIaRepository.findAll();
    }

}
