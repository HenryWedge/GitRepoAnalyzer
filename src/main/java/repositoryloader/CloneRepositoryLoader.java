package repositoryloader;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
public class CloneRepositoryLoader implements RepositoryLoader {
    private static final int waitingInterval = 5;

    private final String repositoryUri;

    private final File outputFile;

    public CloneRepositoryLoader(final String repositoryUri, final File outputFile) {
        this.repositoryUri = repositoryUri;
        this.outputFile = outputFile;
    }

    @Override
    public Git loadRepository() throws IOException, InterruptedException {
        FileUtils.deleteDirectory(outputFile);
        final RepositoryForkingThread repositoryForkingThread = new RepositoryForkingThread(repositoryUri, outputFile);

        final Thread thread = new Thread(repositoryForkingThread);
        System.out.printf("Start cloning repository %s\n", repositoryUri);
        thread.start();

        int i = 0;
        while ( thread.isAlive() ) {
            Thread.sleep(waitingInterval * 1000);
            i += waitingInterval;
            System.out.printf("Cloning repository for %d seconds. Please wait! \n", i);
        }
        thread.join();
        repositoryForkingThread.getResult();

        return repositoryForkingThread.getResult();
    }
}
