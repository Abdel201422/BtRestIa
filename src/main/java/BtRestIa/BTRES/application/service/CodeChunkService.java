package BtRestIa.BTRES.application.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface CodeChunkService {
    List<String> chunkCode(Path repoPath) throws IOException;
}
