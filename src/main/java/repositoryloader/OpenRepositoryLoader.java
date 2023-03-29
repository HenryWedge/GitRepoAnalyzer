package repositoryloader;

import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
public class OpenRepositoryLoader implements RepositoryLoader {

    private final String pathName;

    public OpenRepositoryLoader(final String pathName) {
        this.pathName = pathName;
    }

    @Override
    public Git loadRepository() throws IOException {
        return Git.open(new File(pathName));
    }
}
