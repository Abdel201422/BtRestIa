package BtRestIa.BTRES.infrastructure.dto.request;

import lombok.Data;
import org.stringtemplate.v4.ST;

@Data
public class PreguntaRequestDto {
    private String token;
    private String texto;
    private String modelo;
    private String repoUrl;

    public PreguntaRequestDto() {}

    public PreguntaRequestDto(String token, String texto, String modelo, String repoUrl) {
        this.token = token;
        this.texto = texto;
        this.modelo = modelo;
        this.repoUrl = repoUrl;
    }

}
