package BtRestIa.BTRES.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pregunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String texto;
    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();
}