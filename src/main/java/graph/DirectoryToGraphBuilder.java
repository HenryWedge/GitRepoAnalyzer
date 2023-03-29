package graph;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;
import allen.MyNode;
import com.github.javaparser.ast.CompilationUnit;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import parser.ASTParser;
public abstract class DirectoryToGraphBuilder {
    public Graph<Node<String>> calculate(final String directory) {

        final ASTParser astParser = new ASTParser();

        final List<CompilationUnit> compilationUnits = astParser.getCompilationUnits(directory, null);
        final Map<String, Set<String>> importsGraph = buildGraph(compilationUnits);

        final MutableGraph<Node<String>> graph = GraphBuilder
            .directed()
            .allowsSelfLoops(true)
            .build();

        importsGraph.forEach((key, importModules) -> {
            if (!graph
                .nodes()
                .contains(new MyNode(key))) {
                graph.addNode(new MyNode(key));
            }
            importModules
                .stream()
                .filter(importModule -> filterImportGraph(importsGraph, importModule))
                .forEach(modul -> graph.putEdge(EndpointPair.ordered(new MyNode(key), new MyNode(modul))));
        });
        return graph;
    }

    private String buildNModule(final String packageName, final int n) {
        final List<String> packagePath = Arrays.asList(packageName.split("\\."));

        final StringBuilder modulName = new StringBuilder();
        for ( int i = 0; i < n; i++ ) {
            if (i < packagePath.size()) {
                modulName.append(packagePath.get(i));
                modulName.append(".");
            }
        }

        modulName.delete(modulName.length() - 1, modulName.length());
        return modulName.toString();
    }

    private boolean filterImportGraph(Map<String, Set<String>> importsGraph, final String importModul) {
        return importsGraph
            .keySet()
            .stream()
            .anyMatch(importModul::contains);
    }

    protected abstract Map<String, Set<String>> buildGraph(List<CompilationUnit> compilationUnits);
}
