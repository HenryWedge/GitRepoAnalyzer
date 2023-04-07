package main;

import static java.util.stream.Collectors.toList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.graph.Graph;
import graph.FullyQualifiedGraphBuilder;
import metric.execution.Metric;
import metric.execution.MetricExecutor;
import metric.execution.MetricSettings;
import output.ConsoleOutput;
import output.JsonOutput;
import output.Output;
import repositoryloader.CloneRepositoryLoader;
import repositoryloader.OpenRepositoryLoader;

public class Main {

    @Parameter(names = {"-s", "--skip"}, description = "")
    boolean skipAlreadyAnalysedProjects = false;

    @Parameter(names = {"-r", "--repo"}, description = "")
    String repositoryFileName = "src\\main\\resources\\repository.txt";

    @Parameter(names = {"-o", "--output"}, description = "")
    String resultFile = "src\\main\\resources\\result_corrected.json";

    @Parameter(names = {"-i", "--include"}, description = "")
    List<String> includeList = new ArrayList<>();

    @Parameter(names = {"-e", "--exclude"}, description = "")
    List<String> excludeList = new ArrayList<>();

    @Parameter(names = {"-a", "--all"}, description = "")
    boolean includeAllMetrics = true;

    private void run() throws IOException, InterruptedException {
        final Output out = new ConsoleOutput();
        final List<String> repositoryUris = readRepositoriesFromFile(repositoryFileName);

        for ( final String repositoryUri : repositoryUris ) {
            final String projectName = formatUriToProjectName(repositoryUri);
            final String directory = String.format("output\\%s", projectName);

            final Git git;
            if (new File(directory).exists()) {
                git = new OpenRepositoryLoader(directory).loadRepository();
                if (skipAlreadyAnalysedProjects) {
                    out.println(String.format("Project %s was skipped", projectName));
                    continue;
                }
            } else {
                git = new CloneRepositoryLoader(repositoryUri, new File(directory)).loadRepository();
                out.println("Cloning Finished");
            }

            final Graph<Node<String>> graph = new FullyQualifiedGraphBuilder().calculate(directory);
            final MetricExecutor metricExecutor = new MetricExecutor(git, directory + "\\", graph, projectName, buildMetricSettings());
            metricExecutor.execute();

            System.out.printf("Start analysing: %s\n", projectName);
            System.out.println(metricExecutor);
            new JsonOutput(resultFile).writeResults(projectName, metricExecutor);
            out.println("Finished");
        }
    }

    private MetricSettings buildMetricSettings() {
        final MetricSettings metricSettings;

        if (includeAllMetrics) {
            metricSettings = MetricSettings.createComplete();
        } else if (!includeList.isEmpty()) {
            metricSettings = MetricSettings.createIncluding(includeList
                                                                .stream()
                                                                .map(Metric::valueOf)
                                                                .collect(toList()));
        } else if (!excludeList.isEmpty()) {
            metricSettings = MetricSettings.createIncluding(excludeList
                                                                .stream()
                                                                .map(Metric::valueOf)
                                                                .collect(toList()));
        } else {
            throw new IllegalStateException();
        }
        return metricSettings;
    }


    private List<String> readRepositoriesFromFile(final String fileName) {
        final List<String> repositories = new ArrayList<>();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while ( line != null ) {
                if (!line.startsWith("#")) {
                    repositories.add(line);
                }
                line = reader.readLine();
            }

            reader.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return repositories;
    }

    private String formatUriToProjectName(final String uri) {
        return uri
            .replaceFirst("https://github\\.com/", "")
            .replaceFirst("\\.git", "")
            .replace(".", "-")
            .replace("/", "-");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final Main main = new Main();

        JCommander
            .newBuilder()
            .addObject(main)
            .build()
            .parse(args);

        main.run();
    }

}
