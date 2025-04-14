package BtRestIa.BTRES.domain;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String name;
    private String email;
    @Column(name = "date_created")
    private LocalDate dateCreated;

    public user() {
    }   

    public user(Long id, String token, String name, String email, LocalDate dateCreated) {
        this.id = id;
        this.token = token;
        this.name = name;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
