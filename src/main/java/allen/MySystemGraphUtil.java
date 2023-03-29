package allen;

import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;
import org.mosim.refactorlizar.architecture.evaluation.graphs.SystemGraphUtils;
import com.google.common.graph.Graph;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;

public class MySystemGraphUtil implements SystemGraphUtils<String> {

    @Override
    public MutableGraph<Node<String>> convertToSystemGraph(final Graph<Node<String>> graph) {
        final MutableGraph<Node<String>> systemGraph = Graphs.copyOf(graph);
        systemGraph.addNode(new MyNode(""));
        return systemGraph;
    }
}
