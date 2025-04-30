package BtRestIa.BTRES.application.service;

import BtRestIa.BTRES.domain.Pregunta;
import BtRestIa.BTRES.domain.Respuesta;

import java.util.List;

public interface UsuarioService {

    List<Pregunta> obtenerPreguntasPorUsuario(String userToken);

    List<Respuesta> obtenerRespuestasPorUsuario(String userToken);
}