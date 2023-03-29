package metric;

import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

public class NumberOfBranchesMetric implements Metric<Integer> {

    @Override
    public Integer getResult(final Git git) throws GitAPIException {
        final List<Ref> branchList = git
            .branchList()
            .setListMode(ListMode.REMOTE)
            .call();

        return branchList.size();
    }
}
