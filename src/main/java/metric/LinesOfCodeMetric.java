package metric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
public class LinesOfCodeMetric implements Metric<Integer> {

    final String projectDir;

    public LinesOfCodeMetric(final String projectDir) {
        this.projectDir = projectDir;
    }

    @Override
    public Integer getResult(final Git git) throws GitAPIException, IOException {
        final Repository repository = git.getRepository();
        final TreeWalk treeWalk = new TreeWalk(repository);
        final RevCommit latestCommit = git
            .log()
            .setMaxCount(1)
            .call()
            .iterator()
            .next();
        final RevTree tree = latestCommit.getTree();
        treeWalk.addTree(tree);
        treeWalk.setRecursive(false);

        int linesOfCode = 0;
        while ( treeWalk.next() ) {
            if (treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
            } else {
                if (treeWalk.getPathString().contains(".java")) {
                    try ( Stream<String> lines = Files.lines(Paths.get( projectDir + treeWalk.getPathString())) ) {
                        linesOfCode += lines.count();
                    }
                }
            }
        }

        return linesOfCode;
    }
}
