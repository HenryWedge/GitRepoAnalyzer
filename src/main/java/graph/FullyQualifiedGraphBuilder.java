package graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.github.javaparser.ast.CompilationUnit;
public class FullyQualifiedGraphBuilder extends DirectoryToGraphBuilder {

    final MyGraphBuilder graphBuilder = new MyGraphBuilder("Test");

    @Override
    protected Map<String, Set<String>> buildGraph(List<CompilationUnit> compilationUnits) {
        return graphBuilder.buildFullyQualified(compilationUnits);
    }
}
