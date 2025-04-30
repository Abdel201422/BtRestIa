package BtRestIa.BTRES.infrastructure.dto.request;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getModelo(){ return modelo; }

    public void setModelo(String modelo){ this.modelo = modelo; }
}
