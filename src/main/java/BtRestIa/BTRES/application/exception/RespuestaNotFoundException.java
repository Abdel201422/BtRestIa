package BtRestIa.BTRES.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import BtRestIa.BTRES.application.constants.Constantes;

public class RespuestaNotFoundException extends ResponseStatusException{
    public RespuestaNotFoundException(String token) {
        super(HttpStatus.NOT_FOUND, Constantes.RESPUESTA_NO_ENCONTRADA + token);
    }
}
