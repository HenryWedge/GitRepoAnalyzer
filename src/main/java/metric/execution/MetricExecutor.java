package metric.execution;

import static metric.execution.Metric.ALLEN_COHESION;
import static metric.execution.Metric.ALLEN_COMPLEXITY;
import static metric.execution.Metric.ALLEN_COUPLING;
import static metric.execution.Metric.ALLEN_GRAPH_SIZE;
import static metric.execution.Metric.GIT_AVERAGE_COMMIT_SIZE;
import static metric.execution.Metric.GIT_AVERAGE_FILE_SIZE;
import static metric.execution.Metric.GIT_COMMIT_COUNT;
import static metric.execution.Metric.GIT_CONTRIBUTOR_ENTROPY;
import static metric.execution.Metric.GIT_LINES_OF_CODE;
import static metric.execution.Metric.GIT_NUMBER_OF_CONTRIBUTORS;
import static metric.execution.Metric.GIT_NUMBER_OF_FILES;
import static metric.execution.Metric.GIT_PROJECT_DURATION;
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

    final MetricSettings metricSettings;

    public MetricExecutor(final Git git, final String projectDir, final Graph<?> graph, final String projectName,
                          final MetricSettings metricSettings) {
        this.projectDir = projectDir;
        this.git = git;
        this.graph = graph;
        this.projectName = projectName;
        this.metricSettings = metricSettings;
    }

    public void execute() {
        try {
            if (metricSettings.contains(GIT_AVERAGE_COMMIT_SIZE)) {
                averageCommitSizeMetricResult = new AverageCommitSizeMetric().getResult(git);
            }
            if (metricSettings.contains(GIT_AVERAGE_FILE_SIZE)) {
                averageFileSizeMetricResult = new AverageFileSizeMetric(projectDir).getResult(git);
            }
            if (metricSettings.contains(GIT_COMMIT_COUNT)) {
                commitCountMetricResult = new CommitCountMetric().getResult(git);
            }
            if (metricSettings.contains(GIT_CONTRIBUTOR_ENTROPY)) {
                contributorEntropyMetricResult = new ContributorEntropyMetric().getResult(git);
            }
            if (metricSettings.contains(GIT_LINES_OF_CODE)) {
                linesOfCodeMetricResult = new LinesOfCodeMetric(projectDir).getResult(git);
            }
            if (metricSettings.contains(GIT_NUMBER_OF_CONTRIBUTORS)) {
                numberOfContributorsMetricResult = new NumberOfContributorsMetric().getResult(git);
            }
            if (metricSettings.contains(GIT_NUMBER_OF_FILES)) {
                numberOfFilesMetricResult = new NumberOfFilesMetric().getResult(git);
            }
            if (metricSettings.contains(GIT_PROJECT_DURATION)) {
                projectDurationMetricResult = new ProjectDurationMetric().getResult(git);
            }
            if (metricSettings.contains(ALLEN_GRAPH_SIZE)) {
                graphSize = new HyperGraphSizeCalculator(CalculationMode.NO_OFFSET).calculate(graph);
            }
            if (metricSettings.contains(ALLEN_COMPLEXITY)) {
                complexity = new HyperGraphComplexityCalculator(CalculationMode.NO_OFFSET, new MySystemGraphUtil())
                    .calculate(graph)
                    .getValue();
            }
            if (metricSettings.contains(ALLEN_COUPLING)) {
                coupling = new HyperGraphInterModuleCouplingGenerator(CalculationMode.NO_OFFSET, new MySystemGraphUtil())
                    .calculate(graph)
                    .getValue();
            }
            if (metricSettings.contains(ALLEN_COHESION)) {
                cohesion = new HyperGraphCohesionCalculator(CalculationMode.NO_OFFSET, new MySystemGraphUtil())
                    .calculate(graph)
                    .getValue();
            }
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
