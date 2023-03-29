package metric;

import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class AverageFileSizeMetric implements Metric<Double> {

    final String projectDir;

    public AverageFileSizeMetric(final String projectDir) {
        this.projectDir = projectDir;
    }

    @Override
    public Double getResult(final Git git) throws GitAPIException, IOException {
        return ((double) new LinesOfCodeMetric(projectDir).getResult(git)) /  ((double) new NumberOfFilesMetric().getResult(git));
    }
}
