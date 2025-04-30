package BtRestIa.BTRES.application.service;

import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;

import java.util.List;

public interface ConsultaService {
    /**
     * Procesa la petición de pregunta: valida usuario, guarda pregunta, llama a IA,
     * guarda respuesta, persiste consulta y retorna DTO.
     */
    RespuestaDto procesarPregunta(PreguntaRequestDto dto);


}