package metric;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
public class NumberOfRefactoringCommitsMetric implements Metric<Integer> {

    @Override
    public Integer getResult(final Git git) throws GitAPIException {
        // TODO here
        return null;
    }
}
