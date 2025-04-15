package BtRestIa.BTRES.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "pregunta")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String texto;
    private LocalDateTime fecha = LocalDateTime.now();

   public Pregunta(){

   }

   public Pregunta(Long id, String token, String texto, LocalDateTime fecha) {
        this.id = id;
        this.token = token;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getfecha() {
        return fecha;
    }

    public void setDateCreated(LocalDateTime fecha) {
        this.fecha = fecha;
    }


}
