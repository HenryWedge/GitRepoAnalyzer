package repositoryloader;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class RepositoryForkingThread implements Runnable{

    private final String repositoryUri;

    private final File outputFile;

    private boolean hasResult = false;

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
            hasResult = true;
        } catch ( GitAPIException e ) {
            throw new IllegalStateException(e);
        }
    }

    public boolean hasResult() {
        return hasResult;
    }

    public Git getResult() {
        if (git == null) {
            throw new IllegalStateException("The result ist available");
        }
        return git;
    }
}
