package BtRestIa.BTRES.application.service.impl;

import BtRestIa.BTRES.application.service.GitService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GitServiceImpl implements GitService {

    @Override
    public void clonarRepo(String repoUrl, String destino) throws GitAPIException {
        try {
            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(new File(destino))
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException("Error al clonar el repositorio", e);
        }
    }
}
