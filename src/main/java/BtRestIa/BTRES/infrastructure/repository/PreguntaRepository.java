package BtRestIa.BTRES.infrastructure.repository;

import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    Optional<Pregunta> findByToken(String token);
}