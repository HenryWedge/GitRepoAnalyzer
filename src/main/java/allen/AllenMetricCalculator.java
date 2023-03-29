package allen;

import org.mosim.refactorlizar.architecture.evaluation.CalculationMode;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Cohesion;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Complexity;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Coupling;
import org.mosim.refactorlizar.architecture.evaluation.cohesion.HyperGraphCohesionCalculator;
import org.mosim.refactorlizar.architecture.evaluation.complexity.HyperGraphComplexityCalculator;
import org.mosim.refactorlizar.architecture.evaluation.coupling.HyperGraphInterModuleCouplingGenerator;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;
import org.mosim.refactorlizar.architecture.evaluation.size.HyperGraphSizeCalculator;
import com.google.common.graph.Graph;

public class AllenMetricCalculator {

    public AllenMetric calculate(final Graph<Node<String>> graph) {
        //final Cohesion cohesion = new HyperGraphCohesionCalculator(CalculationMode.NO_OFFSET, new MySystemGraphUtil()).calculate(graph);
        final double graphSize = new HyperGraphSizeCalculator(CalculationMode.NO_OFFSET).calculate(graph);
        final Complexity complexity = new HyperGraphComplexityCalculator(CalculationMode.NO_OFFSET, new MySystemGraphUtil()).calculate(graph);
        final Coupling coupling = new HyperGraphInterModuleCouplingGenerator(CalculationMode.NO_OFFSET, new MySystemGraphUtil()).calculate(graph);
        return new AllenMetric(graphSize, complexity, new Cohesion(0.0d), coupling);
    }

}
