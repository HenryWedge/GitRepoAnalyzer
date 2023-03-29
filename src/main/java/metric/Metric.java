package metric;

import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import metric.execution.GitMetric;
public interface Metric<T> {
    T getResult(final Git git) throws GitAPIException, IOException;
}
