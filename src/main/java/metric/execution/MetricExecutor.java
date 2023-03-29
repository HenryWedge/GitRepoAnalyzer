package metric.execution;

import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.mosim.refactorlizar.architecture.evaluation.CalculationMode;
import org.mosim.refactorlizar.architecture.evaluation.cohesion.HyperGraphCohesionCalculator;
import org.mosim.refactorlizar.architecture.evaluation.complexity.HyperGraphComplexityCalculator;
import org.mosim.refactorlizar.architecture.evaluation.coupling.HyperGraphInterModuleCouplingGenerator;
import org.mosim.refactorlizar.architecture.evaluation.size.HyperGraphSizeCalculator;
import allen.MySystemGraphUtil;
import com.google.common.graph.Graph;
import metric.AverageCommitSizeMetric;
import metric.AverageFileSizeMetric;
import metric.CommitCountMetric;
import metric.ContributorEntropyMetric;
import metric.LinesOfCodeMetric;
import metric.NumberOfBranchesMetric;
import metric.NumberOfContributorsMetric;
import metric.NumberOfFilesMetric;
import metric.ProjectDurationMetric;
public class MetricExecutor {
    Double averageCommitSizeMetricResult;

    Double averageFileSizeMetricResult;

    Integer commitCountMetricResult;

    Double contributorEntropyMetricResult;

    Integer linesOfCodeMetricResult;

    Integer numberOfBranchesMetricResult;

    Integer numberOfContributorsMetricResult;

    Integer numberOfFilesMetricResult;

    Integer projectDurationMetricResult;

    Double graphSize;

    Double complexity;

    Double coupling;

    Double cohesion;

    final String projectDir;

    final Git git;

    final Graph<?> graph;

    final String projectName;

    public MetricExecutor(final Git git, final String projectDir, final Graph<?> graph, final String projectName) {
        this.projectDir = projectDir;
        this.git = git;
        this.graph = graph;
        this.projectName = projectName;
    }

    public void execute() {
        try {
            averageCommitSizeMetricResult = new AverageCommitSizeMetric().getResult(git);
            averageFileSizeMetricResult = new AverageFileSizeMetric(projectDir).getResult(git);
            commitCountMetricResult = new CommitCountMetric().getResult(git);
            contributorEntropyMetricResult = new ContributorEntropyMetric().getResult(git);
            linesOfCodeMetricResult = new LinesOfCodeMetric(projectDir).getResult(git);
            numberOfBranchesMetricResult = new NumberOfBranchesMetric().getResult(git);
            numberOfContributorsMetricResult = new NumberOfContributorsMetric().getResult(git);
            numberOfFilesMetricResult = new NumberOfFilesMetric().getResult(git);
            projectDurationMetricResult = new ProjectDurationMetric().getResult(git);
            graphSize = new HyperGraphSizeCalculator(CalculationMode.NO_OFFSET).calculate(graph);
            complexity = new HyperGraphComplexityCalculator(CalculationMode.NO_OFFSET, new MySystemGraphUtil())
                .calculate(graph)
                .getValue();
            coupling = new HyperGraphInterModuleCouplingGenerator(CalculationMode.NO_OFFSET, new MySystemGraphUtil())
                .calculate(graph)
                .getValue();
            cohesion = new HyperGraphCohesionCalculator(CalculationMode.NO_OFFSET, new MySystemGraphUtil())
                .calculate(graph)
                .getValue();
        } catch ( final GitAPIException | IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "MetricExecutor{" +
            "averageCommitSizeMetricResult=" + averageCommitSizeMetricResult +
            ", averageFileSizeMetricResult=" + averageFileSizeMetricResult +
            ", commitCountMetricResult=" + commitCountMetricResult +
            ", contributorEntropyMetricResult=" + contributorEntropyMetricResult +
            ", linesOfCodeMetricResult=" + linesOfCodeMetricResult +
            ", numberOfBranchesMetricResult=" + numberOfBranchesMetricResult +
            ", numberOfContributorsMetricResult=" + numberOfContributorsMetricResult +
            ", numberOfFilesMetricResult=" + numberOfFilesMetricResult +
            ", projectDurationMetricResult=" + projectDurationMetricResult +
            ", graphSize=" + graphSize +
            ", complexity=" + complexity +
            ", coupling=" + coupling +
            ", cohesion=" + cohesion +
            '}';
    }

    public String getProjectName() {
        return projectName;
    }

    public Double getAverageCommitSizeMetricResult() {
        return averageCommitSizeMetricResult;
    }

    public Double getAverageFileSizeMetricResult() {
        return averageFileSizeMetricResult;
    }

    public Integer getCommitCountMetricResult() {
        return commitCountMetricResult;
    }

    public Double getContributorEntropyMetricResult() {
        return contributorEntropyMetricResult;
    }

    public Integer getLinesOfCodeMetricResult() {
        return linesOfCodeMetricResult;
    }

    public Integer getNumberOfBranchesMetricResult() {
        return numberOfBranchesMetricResult;
    }

    public Integer getNumberOfContributorsMetricResult() {
        return numberOfContributorsMetricResult;
    }

    public Integer getNumberOfFilesMetricResult() {
        return numberOfFilesMetricResult;
    }

    public Integer getProjectDurationMetricResult() {
        return projectDurationMetricResult;
    }

    public Double getGraphSize() {
        return graphSize;
    }

    public Double getComplexity() {
        return complexity;
    }

    public Double getCoupling() {
        return coupling;
    }

    public Double getCohesion() {
        return cohesion;
    }
}
