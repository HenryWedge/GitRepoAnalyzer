package repositoryloader;

import java.io.IOException;
import org.eclipse.jgit.api.Git;

public interface RepositoryLoader {
    Git loadRepository() throws IOException, InterruptedException;
}
