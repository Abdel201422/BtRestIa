package BtRestIa.BTRES.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import BtRestIa.BTRES.application.constants.Constantes;

public class UsuarioNotFoundException extends ResponseStatusException {
    public UsuarioNotFoundException(String token) {
        super(HttpStatus.NOT_FOUND, Constantes.USUARIO_NO_ENCONTRADO + token);
    }
}