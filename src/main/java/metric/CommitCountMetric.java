package metric;

import java.util.ArrayList;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

public class CommitCountMetric implements Metric<Integer> {

    @Override
    public Integer getResult(final Git git) throws GitAPIException {
        final ArrayList<RevCommit> commits = new ArrayList<>();
        git
            .log()
            .call()
            .forEach(commits::add);
        return commits.size();
    }
}
