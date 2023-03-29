package search;

import org.eclipse.jgit.api.errors.GitAPIException;
public interface SearchStrategy {
    void checkoutNextCommit() throws GitAPIException;

    boolean isSearchFinished();

    default void execute(final GitConsumer gitConsumer) throws GitAPIException {
        while ( !isSearchFinished() ) {
            checkoutNextCommit();
            gitConsumer.accept();
        }
    }

    @FunctionalInterface
    interface GitConsumer {
        void accept() throws GitAPIException;
    }
}
