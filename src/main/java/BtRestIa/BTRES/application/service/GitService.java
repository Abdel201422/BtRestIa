package BtRestIa.BTRES.application.service;

import org.eclipse.jgit.api.errors.GitAPIException;

public interface GitService {

    void clonarRepo(String repoUrl, String destino) throws GitAPIException;
}
