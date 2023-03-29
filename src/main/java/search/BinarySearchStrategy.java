package search;

import java.util.List;
import org.eclipse.jgit.revwalk.RevCommit;

public class BinarySearchStrategy implements SearchStrategy {
    private List<RevCommit> commits;

    public BinarySearchStrategy(final List<RevCommit> commits) {
        this.commits = commits;
    }

    @Override
    public void checkoutNextCommit() {
        // TODO
    }

    @Override
    public boolean isSearchFinished() {
        return true;
    }

}
