package BtRestIa.BTRES.application.service;

import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;
import BtRestIa.BTRES.infrastructure.dto.request.PreguntaRequestDto;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface ConsultaService {
    /**
     * Procesa la petición de pregunta: valida usuario, guarda pregunta, llama a IA,
     * guarda respuesta, persiste consulta y retorna DTO.
     */
    RespuestaDto procesarPregunta(PreguntaRequestDto dto) throws GitAPIException, IOException;

    RespuestaDto obtenerRespuestaPorToken(String token);

    PreguntaDto obtenerPreguntaPorToken(String token);
}