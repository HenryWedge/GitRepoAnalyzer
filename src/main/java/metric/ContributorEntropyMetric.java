package metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import git.commit.CommitList;

public class ContributorEntropyMetric implements Metric<Double> {

    @Override
    public Double getResult(final Git git) throws GitAPIException {
        final ArrayList<RevCommit> commits = new ArrayList<>();
        git
            .log()
            .call()
            .forEach(commits::add);

        final Map<String, List<RevCommit>> committerIdentificationMap = CommitList
            .get(git)
            .stream()
            .collect(Collectors.groupingBy(commit -> commit
                .getCommitterIdent()
                .getEmailAddress()));

        return -committerIdentificationMap
            .values()
            .stream()
            .map(List::size)
            .map(i -> (double) i / (double) commits.size())
            .map(px -> px * Math.log(px))
            .reduce(Double::sum)
            .orElseThrow(IllegalStateException::new);
    }
}
