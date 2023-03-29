package search;

import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import git.commit.CommitList;
public class LinearSearchStrategy implements SearchStrategy {

    private final int numberOfCommits;
    private int currentCommit = 0;
    private final Git git;

    public LinearSearchStrategy(final int numberOfCommits, final Git git) {
        this.numberOfCommits = numberOfCommits;
        this.git = git;
    }

    @Override
    public void checkoutNextCommit() throws GitAPIException {
        final List<RevCommit> commits = CommitList.get(git);
        git
            .checkout()
            .setName(commits
                         .get(currentCommit)
                         .getName())
            .call();
        currentCommit++;
    }

    @Override
    public boolean isSearchFinished() {
        return numberOfCommits == currentCommit;
    }
}
