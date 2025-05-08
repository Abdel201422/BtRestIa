package BtRestIa.BTRES.infrastructure.dto.request;

import lombok.Data;

@Data
public class PreguntaRequestDto {
    private String token;
    private String texto;
    private String modelo;

    public PreguntaRequestDto() {}

    public PreguntaRequestDto(String token, String texto, String modelo) {
        this.token = token;
        this.texto = texto;
        this.modelo = modelo;
    }

}
