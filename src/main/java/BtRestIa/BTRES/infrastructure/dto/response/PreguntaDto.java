package BtRestIa.BTRES.infrastructure.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
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
