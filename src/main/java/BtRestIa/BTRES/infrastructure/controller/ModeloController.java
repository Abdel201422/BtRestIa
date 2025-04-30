package BtRestIa.BTRES.infrastructure.controller;

import org.springframework.web.bind.annotation.*;

import BtRestIa.BTRES.application.service.ModeloService;

@RestController
@RequestMapping("/ai")
public class ModeloController {

    private final ModeloService modeloService;

    
    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }
    
    @PostMapping("/llama3")
    public String askLlama3(@RequestBody String prompt) {
        return "Modelo: llama3 | Respuesta: " + modeloService.askToLlama3(prompt);
    }

    // Endpoint específico para mistral
    @PostMapping("/mistral")
    public String askMistral(@RequestBody String prompt) {
        return "Modelo: mistral | Respuesta: " + modeloService.askToMistral(prompt);
    }

    
}
