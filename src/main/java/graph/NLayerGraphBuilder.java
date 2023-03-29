package graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.github.javaparser.ast.CompilationUnit;
public class NLayerGraphBuilder extends DirectoryToGraphBuilder {

    final int n;

    public NLayerGraphBuilder(final int n) {
        this.n = n;
    }

    final MyGraphBuilder graphBuilder = new MyGraphBuilder("Test");

    @Override
    protected Map<String, Set<String>> buildGraph(final List<CompilationUnit> compilationUnits) {
        return graphBuilder.buildNLayer(compilationUnits, n);
    }
}
