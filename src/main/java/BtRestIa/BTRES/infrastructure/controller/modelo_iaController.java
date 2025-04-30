package BtRestIa.BTRES.infrastructure.controller;

import BtRestIa.BTRES.domain.ModeloIA;
import BtRestIa.BTRES.infrastructure.repository.Modelo_iaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class modelo_iaController {

    @Autowired
    private Modelo_iaRepository modelo_iaRepository;

    @GetMapping("/api/modelo_ia/modelo")
    public List<ModeloIA> getModelo_iaRepository() {
        return modelo_iaRepository.findAll();
    }


}
