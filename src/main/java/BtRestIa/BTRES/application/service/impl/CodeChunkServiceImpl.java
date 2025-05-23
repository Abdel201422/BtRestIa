package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.application.service.CodeChunkService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeChunkServiceImpl implements CodeChunkService {

    private static final int LINES_PER_CHUNK = 10;

    /**
     * Recorre recursivamente el repositorio y chunkea cada archivo de código
     * en bloques de N líneas.
     *
     * @param repoPath la carpeta raíz del repo clonado
     * @return lista de fragmentos de código
     * @throws IOException si hay error de I/O
     */
    @Override
    public List<String> chunkCode(Path repoPath) throws IOException {
        List<String> chunks = new ArrayList<>();

        // Recorremos todos los ficheros regulares
        Files.walk(repoPath)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String name = path.getFileName().toString().toLowerCase();
                    // Filtramos solo extensiones de código
                    return name.endsWith(".java") || name.endsWith(".js") || name.endsWith(".py");
                })
                .forEach(path -> {
                    try {
                        List<String> lines = Files.readAllLines(path);
                        // Agrupamos cada N líneas en un fragmento
                        for (int start = 0; start < lines.size(); start += LINES_PER_CHUNK) {
                            int end = Math.min(start + LINES_PER_CHUNK, lines.size());
                            // Unimos las líneas en un solo string
                            String chunk = String.join(
                                    System.lineSeparator(),
                                    lines.subList(start, end)
                            );
                            chunks.add(chunk);
                        }
                    } catch (IOException e) {
                        // Manejo simple de error: continuamos con el siguiente archivo
                        e.printStackTrace();
                    }
                });

        return chunks;
    }
}