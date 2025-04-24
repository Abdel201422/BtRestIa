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
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.execution.DefaultToolExecutionExceptionProcessor;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.resolution.StaticToolCallbackResolver;
import org.springframework.ai.tool.resolution.ToolCallbackResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;

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
    // Encuentra el método de la herramienta
    Method toolMethod = ReflectionUtils.findMethod(BuscarUsuarioTool.class, "buscarUsuarioPorNombre", String.class);

    // Crea el ToolCallback usando MethodToolCallback
    ToolCallback buscarUsuarioToolCallback = MethodToolCallback.builder()
        .toolDefinition(ToolDefinition.builder()
            .name("buscarUsuarioPorNombre")
            .description("Busca un usuario por su nombre en la base de datos")
            .inputSchema("{\"type\": \"string\"}") // Esquema de entrada en formato JSON
            .build())
        .toolMethod(toolMethod) // Método de la herramienta
        .toolObject(buscarUsuarioTool) // Instancia de la herramienta
        .build();

    // Crea el ToolCallbackResolver
    ToolCallbackResolver resolver = new StaticToolCallbackResolver(Arrays.asList(buscarUsuarioToolCallback));

    // Configura el ToolCallingManager
    ToolCallingManager toolCallingManager = new DefaultToolCallingManager(
        ObservationRegistry.NOOP,
        resolver,
        DefaultToolExecutionExceptionProcessor.builder().build()
    );

    // Construye el modelo de chat
    return OllamaChatModel.builder()
            .ollamaApi(sharedOllamaApi)
            .defaultOptions(OllamaOptions.builder()
                    .model("llama3")
                    .temperature(0.3)
                    .build())
            .toolCallingManager(toolCallingManager)
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
