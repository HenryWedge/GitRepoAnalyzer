package git.commit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

public class CommitList {
    public static List<RevCommit> get(final Git git) {
        final ArrayList<RevCommit> commits = new ArrayList<>();
        try {
            git
                .log()
                .call()
                .forEach(commits::add);
            return commits;
        } catch ( final GitAPIException e ) {
            return Collections.emptyList();
        }
    }
}
