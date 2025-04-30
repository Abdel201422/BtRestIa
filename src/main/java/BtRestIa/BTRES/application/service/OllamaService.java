package BtRestIa.BTRES.application.service;

public interface OllamaService {
    /**
     * Llama al endpoint HTTP de Ollama para generar una respuesta para el prompt dado
     * @param modelName nombre del modelo (e.g. "ollama3" o "mistral")
     * @param prompt    el texto de la pregunta
     * @return          la respuesta generada por el motor
     */
    String generarRespuesta(String modelName, String prompt);
}