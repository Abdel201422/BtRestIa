package BtRestIa.BTRES.infrastructure.repository;

import BtRestIa.BTRES.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findyByNombre(String nombreUsuario);
}