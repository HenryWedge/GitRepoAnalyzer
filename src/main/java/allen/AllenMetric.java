package allen;

import java.text.DecimalFormat;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Cohesion;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Complexity;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Coupling;

public class AllenMetric {

    private final double graphSize;
    private final Complexity complexity;
    private final Cohesion cohesion;
    private final Coupling coupling;

    public AllenMetric(final double graphSize, final Complexity complexity, final Cohesion cohesion,
                       final Coupling coupling) {
        this.graphSize = graphSize;
        this.complexity = complexity;
        this.cohesion = cohesion;
        this.coupling = coupling;
    }

    @Override
    public String toString() {
        final DecimalFormat decimalFormat = new DecimalFormat("0.00");

        return "AllenMetric{" +
            "graphSize=" + decimalFormat.format(graphSize) +
            "; complexity=" + decimalFormat.format(complexity.getValue()) +
            "; cohesion=" + decimalFormat.format(cohesion.getValue()) +
            "; coupling=" + decimalFormat.format(coupling.getValue()) +
            '}';
    }
}
