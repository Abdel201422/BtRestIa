package BtRestIa.BTRES.domain;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "pregunta")
public class pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String texto;
    @Column(name = "date_created")
    private LocalDate dateCreated;

   public pregunta(){

   }

   public pregunta(Long id, String token, String texto, LocalDate dateCreated) {
        this.id = id;
        this.token = token;
        this.texto = texto;
        this.dateCreated = dateCreated;
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

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }


}
