package metric;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
public class ProjectDurationMetric implements Metric<Integer> {

    @Override
    public Integer getResult(final Git git) throws GitAPIException {
        final List<RevCommit> commits = new ArrayList<>();
        git.log().call().forEach(commits::add);
        final RevCommit initialCommit = commits.get(0);
        final RevCommit lastCommit = commits.get(commits.size() - 1);
        return initialCommit.getCommitTime() - lastCommit.getCommitTime();
    }
}
