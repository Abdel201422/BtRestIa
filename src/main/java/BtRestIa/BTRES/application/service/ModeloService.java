package BtRestIa.BTRES.application.service;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class ModeloService {
    private final OllamaChatModel llama3Model;
    private final OllamaChatModel mistralModel;


    public ModeloService(OllamaChatModel llama3Model, OllamaChatModel mistralModel) {
        this.llama3Model = llama3Model;
        this.mistralModel = mistralModel;
        
    }

    public String askToLlama3(String request) {
        return llama3Model.call(request);
    }

    public String askToMistral(String request) {
        return mistralModel.call(request);
    }
}