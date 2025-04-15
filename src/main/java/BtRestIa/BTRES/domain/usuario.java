package BtRestIa.BTRES.domain;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String nombre;
    private String email;
    @Column(name = "date_created")
    private LocalDate dateCreated;

    public usuario() {
    }   

    public usuario(Long id, String token, String nombre, String email, LocalDate dateCreated) {
        this.id = id;
        this.token = token;
        this.nombre = nombre;
        this.email = email;
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

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }   
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }



}
