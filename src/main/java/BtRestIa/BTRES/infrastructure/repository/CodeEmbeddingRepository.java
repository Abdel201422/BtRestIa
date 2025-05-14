package BtRestIa.BTRES.infrastructure.repository;

import BtRestIa.BTRES.domain.CodeEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeEmbeddingRepository extends JpaRepository<CodeEmbedding, Long> {
    // Puedes añadir métodos de filtrado por repoName si lo necesitas
}
