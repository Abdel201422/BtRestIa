package BtRestIa.BTRES.infrastructure.repository;

import BtRestIa.BTRES.domain.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    Optional<Respuesta> findByToken(String token);

}