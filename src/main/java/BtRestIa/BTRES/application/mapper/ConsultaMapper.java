package BtRestIa.BTRES.application.mapper;

import BtRestIa.BTRES.domain.Consulta;

import BtRestIa.BTRES.infrastructure.dto.response.PreguntaDto;
import BtRestIa.BTRES.infrastructure.dto.response.RespuestaDto;

public class ConsultaMapper {

    private ConsultaMapper() {}

    public static PreguntaDto toPreguntaDto(Consulta consulta) {
        return PreguntaDto.fromEntity(consulta.getPregunta());
    }

    public static RespuestaDto toRespuestaDto(Consulta consulta) {
        return RespuestaDto.fromEntity(consulta.getRespuesta());
    }
}
