package metric;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import metric.util.CommitListUtility;
public class NumberOfContributorsMetric implements Metric<Integer> {

    @Override
    public Integer getResult(final Git git) throws GitAPIException {
        final Map<String, List<RevCommit>> committerIdentificationMap = CommitListUtility
            .get(git)
            .stream()
            .collect(Collectors.groupingBy(commit -> commit
                .getCommitterIdent()
                .getEmailAddress()));

        return committerIdentificationMap.size();
    }
}
