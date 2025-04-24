package BtRestIa.BTRES;

import java.util.Arrays;
import java.util.List;

import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.ai.tool.resolution.StaticToolCallbackResolver;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import BtRestIa.BTRES.application.service.BuscarUsuarioTool;
import io.micrometer.observation.ObservationRegistry;

@SpringBootApplication
public class BtresApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtresApplication.class, args);
	}

	@Bean
    public OllamaApi sharedOllamaApi() {
        return new OllamaApi("http://localhost:11434"); 
    }

	@Bean
	@Primary
    public OllamaChatModel llama3ChatModel(OllamaApi sharedOllamaApi, BuscarUsuarioTool buscarUsuarioTool) {
       //  List<ToolCallback> toolCallbacks = ToolCallbacks.from(buscarUsuarioTool); // Register the tool here
       


        return OllamaChatModel.builder()
                .ollamaApi(sharedOllamaApi)
                .defaultOptions(OllamaOptions.builder()
                        .model("llama3") 
                        .build())
                .toolCallingManager(ToolCallingManager.builder()
                
                ) 
                
                .observationRegistry(ObservationRegistry.NOOP)
                .modelManagementOptions(ModelManagementOptions.defaults())
                .build();
    }

	@Bean
    public OllamaChatModel mistralChatModel(OllamaApi sharedOllamaApi) {
        return OllamaChatModel.builder()
                .ollamaApi(sharedOllamaApi)
                .defaultOptions(OllamaOptions.builder()
                        .model("mistral") 
                        .build())
                .toolCallingManager(ToolCallingManager.builder().build())
                .observationRegistry(ObservationRegistry.NOOP)
                .modelManagementOptions(ModelManagementOptions.defaults())
                .build();
    }

}
