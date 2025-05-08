package BtRestIa.BTRES.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import BtRestIa.BTRES.application.constants.Constantes;

public class ModeloAiNotFoundException extends ResponseStatusException {
    
    public ModeloAiNotFoundException() {
        super(HttpStatus.NOT_FOUND, Constantes.MODELO_NO_DISPONIBLE);
    }

    public ModeloAiNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
