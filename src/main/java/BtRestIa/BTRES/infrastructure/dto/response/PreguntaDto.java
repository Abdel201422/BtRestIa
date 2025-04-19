package BtRestIa.BTRES.infrastructure.dto.response;

import java.time.LocalDateTime;

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

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public static PreguntaDto fromEntity(BtRestIa.BTRES.domain.Pregunta pregunta) {
        return new PreguntaDto(
                pregunta.getToken(),
                pregunta.getTexto(),
                pregunta.getfecha()
        );
    }
}
