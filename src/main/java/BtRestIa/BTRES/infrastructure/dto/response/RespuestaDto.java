package BtRestIa.BTRES.infrastructure.dto.response;

import java.time.LocalDateTime;

public class RespuestaDto {
    private String token;
    private String texto;
    private LocalDateTime fecha;

    public RespuestaDto() {}

    public RespuestaDto(String token, String texto, LocalDateTime fecha) {
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

    public static RespuestaDto fromEntity(BtRestIa.BTRES.domain.Respuesta respuesta) {
        return new RespuestaDto(
                respuesta.getToken(),
                respuesta.getTexto(),
                respuesta.getFecha()
        );
    }

    public static RespuestaDto of(String token, String texto, LocalDateTime fecha) {
        RespuestaDto dto = new RespuestaDto();
        dto.setToken(token);
        dto.setTexto(texto);
        dto.setFecha(fecha);
        return dto;
    }
}
