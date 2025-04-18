package BtRestIa.BTRES.application.service;

import BtRestIa.BTRES.domain.Usuario;

public interface TokenService {
    /**
     * Valida el token y devuelve el Usuario asociado.
     * Lanza excepción si no existe.
     */
    Usuario validateUsuarioToken(String token);

    /**
     * Genera un nuevo token para el usuario y lo persiste.
     */
    String generateTokenForUsuario(Usuario usuario);
}
