package BtRestIa.BTRES.application.service;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class ModeloService {
    private final OllamaChatModel llama3Model;
    private final OllamaChatModel mistralModel;
    private final BuscarUsuarioTool buscarUsuarioTool;

    public ModeloService(OllamaChatModel llama3Model, OllamaChatModel mistralModel,
            BuscarUsuarioTool buscarUsuarioTool) {
        this.llama3Model = llama3Model;
        this.mistralModel = mistralModel;
        this.buscarUsuarioTool = buscarUsuarioTool;

    }

    public String askToLlama3(String request) {
        if (debeUsarBuscarUsuarioTool(request)) {
            String nombre = extraerNombreDePrompt(request);
            return buscarUsuarioTool.buscarUsuarioPorNombre(nombre);
        }

        return llama3Model.call(request);
    }

    public String askToMistral(String request) {
        return mistralModel.call(request);
    }

    private boolean debeUsarBuscarUsuarioTool(String request) {
        return request.toLowerCase().contains("usuario") && request.toLowerCase().contains("nombre");
    }

    private String extraerNombreDePrompt(String prompt) {
        if (prompt.contains("nombre")) {
            return prompt.substring(prompt.indexOf("nombre") + 7).replace("?", "").trim();
        }
        return "";
    }

}