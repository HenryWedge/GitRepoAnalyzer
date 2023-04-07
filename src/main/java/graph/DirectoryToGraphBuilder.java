package graph;

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
public class DirectoryToGraphBuilder {

    
    public Graph<Node<String>> calculate(final String directory) {

        final ASTParser astParser = new ASTParser();

        final List<CompilationUnit> compilationUnits = astParser.getCompilationUnits(directory, null);
        final Map<String, Set<String>> importsGraph = new MyGraphBuilder("Test").buildFullyQualified(compilationUnits);

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

    private boolean filterImportGraph(Map<String, Set<String>> importsGraph, final String importModul) {
        return importsGraph
            .keySet()
            .stream()
            .anyMatch(importModul::contains);
    }

}
