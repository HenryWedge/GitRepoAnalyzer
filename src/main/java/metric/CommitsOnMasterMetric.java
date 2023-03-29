package metric;

import java.util.Arrays;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;

public class CommitsOnMasterMetric implements Metric<Integer> {
    private final List<String> mainBranchPatterns = Arrays.asList("main", "master");

    @Override
    public Integer getResult(final Git git) throws GitAPIException {
        // TODO here

        final ListBranchCommand mainBranch = mainBranchPatterns
            .stream()
            .map(branchName -> git
                .branchList()
                .setContains(branchName))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No main branch found"));

        return mainBranch.call().size();
    }
}
