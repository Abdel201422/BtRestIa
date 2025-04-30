package BtRestIa.BTRES.infrastructure.controller;

import BtRestIa.BTRES.application.service.ConsultaService;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consulta")
@CrossOrigin("*")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/preguntar")
    public ResponseEntity<RespuestaDto> preguntar(@RequestBody PreguntaRequestDto dto) {
        RespuestaDto respuesta = consultaService.procesarPregunta(dto);
        return ResponseEntity.ok(respuesta);
    }
}
