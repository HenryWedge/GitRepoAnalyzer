package metric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
public class AverageCommitSizeMetric implements Metric<Double> {
    @Override
    public Double getResult(final Git git) throws GitAPIException {
        List<RevCommit> commits = new ArrayList<>();
        git
            .log()
            .call()
            .forEach(commits::add);

        return ((double) commits
            .stream()
            .map(commit -> {
                try {
                    return getNumberOfAffectedFiles(git, commit);
                } catch ( IOException e ) {
                    return 0;
                }
            })
            .reduce(0, Integer::sum)) / ((double) new CommitCountMetric().getResult(git));
    }

    public static int getNumberOfAffectedFiles(final Git git, final RevCommit commit)
        throws IOException {

        final Repository repository = git.getRepository();
        final TreeWalk treeWalk = new TreeWalk(repository);
        final RevTree tree = commit.getTree();
        treeWalk.addTree(tree);
        treeWalk.setRecursive(false);

        int affectedFiles = 0;
        while ( treeWalk.next() ) {
            if (treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
            } else {
                affectedFiles++;
            }
        }

        return affectedFiles;
    }
}
