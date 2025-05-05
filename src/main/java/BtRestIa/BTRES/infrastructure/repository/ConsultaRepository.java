package BtRestIa.BTRES.infrastructure.repository;

import BtRestIa.BTRES.domain.Consulta;
import BtRestIa.BTRES.domain.Respuesta;
import BtRestIa.BTRES.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByUsuario(Usuario usuario);

}