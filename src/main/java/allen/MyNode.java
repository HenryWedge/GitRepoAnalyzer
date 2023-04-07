package allen;

import java.util.Arrays;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;
public class MyNode implements Node<String> {
    private final String packageName;

    private final String className;

    public MyNode(final String module) {
        final String[] split = module.split("\\.");

        this.packageName = Arrays
            .asList(split)
            .subList(0, split.length - 1)
            .stream()
            .reduce((a, b) -> String.format("%s.%s", a, b))
            .orElse("");
        this.className = split[split.length - 1];
    }

    @Override
    public String getModule() {
        return packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof MyNode))
            return false;
        final MyNode node = (MyNode) o;
        return node
            .getPackageName()
            .equals(packageName) && node
            .getClassName()
            .equals(className);
    }

    @Override
    public int hashCode() {
        return packageName.hashCode() + className.hashCode();
    }
}
