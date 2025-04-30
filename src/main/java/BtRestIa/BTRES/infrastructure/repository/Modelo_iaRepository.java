package BtRestIa.BTRES.infrastructure.repository;

import BtRestIa.BTRES.domain.ModeloIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface Modelo_iaRepository extends JpaRepository<ModeloIA, Long> {
    List<ModeloIA> findAll();

    Optional<ModeloIA> findByNombreAndActivoTrue(String modelo);
}
