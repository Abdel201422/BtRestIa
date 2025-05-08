package BtRestIa.BTRES.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import BtRestIa.BTRES.application.constants.Constantes;

public class PreguntaNotFoundException extends ResponseStatusException {
    public PreguntaNotFoundException(String token) {
        super(HttpStatus.NOT_FOUND, Constantes.PREGUNTA_NO_ENCONTRADA + token);
    }
}