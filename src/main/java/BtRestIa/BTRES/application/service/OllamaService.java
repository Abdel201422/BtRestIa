package BtRestIa.BTRES.application.service;

public interface OllamaService {

        /**
         * Llama a Ollama para obtener el embedding de un texto.
         * @param modelName nombre del modelo (e.g. "llama3")
         * @param text      el fragmento de código o la pregunta
         * @return          vector numérico (double[])
         */
        double[] embed(String modelName, String text);



}