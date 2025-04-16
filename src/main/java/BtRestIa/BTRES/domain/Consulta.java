package BtRestIa.BTRES.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "consulta")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "modelo_id")
    private ModeloIA modeloIA;

    @OneToOne
    @JoinColumn(name = "respuesta_id")
    private Respuesta respuesta;

    @OneToOne
    @JoinColumn(name = "pregunta_id")
    private Pregunta pregunta;

    public Consulta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ModeloIA getModeloIA() {
        return modeloIA;
    }

    public void setModeloIA(ModeloIA modeloIA) {
        this.modeloIA = modeloIA;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

}
