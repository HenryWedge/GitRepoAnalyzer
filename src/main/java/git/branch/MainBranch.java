package git.branch;

import java.util.Arrays;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

public class MainBranch {
    private static final List<String> mainBranchPatterns = Arrays.asList("main", "master");

    //public static List<Ref> get(Git git) {
    //    return mainBranchPatterns
    //        .stream()
    //        .map(branchName -> git
    //            .checkout()
    //            .setName("master")
    //        .findFirst()
    //        .map(cmd -> {
    //            try {
    //                return cmd.call();
    //            } catch ( final GitAPIException ignore ) {
    //                throw new IllegalStateException("No main branch found");
    //            }
    //        }).orElseThrow(() -> new IllegalStateException("No main branch found"));
    //}
}
