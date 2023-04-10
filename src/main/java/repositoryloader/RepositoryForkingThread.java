package repositoryloader;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class RepositoryForkingThread implements Runnable{

    private final String repositoryUri;

    private final File outputFile;

    private Git git;

    public RepositoryForkingThread(final String repositoryUri, final File outputFile) {
        this.repositoryUri = repositoryUri;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        try {
            git = Git
                .cloneRepository()
                .setURI(repositoryUri)
                .setDirectory(outputFile)
                .call();
        } catch ( final GitAPIException e ) {
            throw new IllegalStateException(e);
        }
    }

    public Git getResult() {
        if (git == null) {
            throw new IllegalStateException("The result ist available");
        }
        return git;
    }
}
