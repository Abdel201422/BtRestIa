package BtRestIa.BTRES.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "code_embeddings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CodeEmbedding {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoName;
    private String filePath;

    @Column(columnDefinition = "TEXT")
    private String fragment;

    @Column(columnDefinition = "TEXT")
    private String embeddingJson;  // JSON array de floats
}
