package BtRestIa.BTRES.infrastructure.repository;

import BtRestIa.BTRES.domain.ModeloIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloIaRepository extends JpaRepository<ModeloIA, Long> {
    
    @NonNull
    List<ModeloIA> findAll();

    Optional<ModeloIA> findByNombreAndActivoTrue(String modelo);
}
