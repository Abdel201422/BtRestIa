package BtRestIa.BTRES.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consulta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}