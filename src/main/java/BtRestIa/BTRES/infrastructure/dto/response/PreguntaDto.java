package BtRestIa.BTRES.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PreguntaDto {
    private String token;
    private String texto;
    private LocalDateTime fecha;

    public PreguntaDto() {}

    public PreguntaDto(String token, String texto, LocalDateTime fecha) {
        this.token = token;
        this.texto = texto;
        this.fecha = fecha;
    }

    public static PreguntaDto fromEntity(BtRestIa.BTRES.domain.Pregunta pregunta) {
        return new PreguntaDto(
                pregunta.getToken(),
                pregunta.getTexto(),
                pregunta.getFecha()
        );
    }
}
