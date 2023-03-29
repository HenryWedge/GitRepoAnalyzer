package main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.mosim.refactorlizar.architecture.evaluation.CalculationMode;
import org.mosim.refactorlizar.architecture.evaluation.cohesion.HyperGraphCohesionCalculator;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;
import allen.MySystemGraphUtil;
import com.google.common.graph.Graph;
import graph.FullyQualifiedGraphBuilder;
import metric.execution.MetricExecutor;
import output.ConsoleOutput;
import output.JsonOutput;
import output.Output;
import repositoryloader.CloneRepositoryLoader;
import repositoryloader.OpenRepositoryLoader;
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final Output out = new ConsoleOutput();
        final boolean skipAlreadyAnalysedProjects = false;
        final List<String> repositoryUris = getRepositoryUris();

        final Graph<Node<String>> graph2 = new FullyQualifiedGraphBuilder().calculate("output\\test");
        new HyperGraphCohesionCalculator<>(CalculationMode.NO_OFFSET, new MySystemGraphUtil()).calculate(graph2);

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
            new HyperGraphCohesionCalculator<>(CalculationMode.NO_OFFSET, new MySystemGraphUtil()).calculate(graph);

            final MetricExecutor metricExecutor = new MetricExecutor(git, directory + "\\", graph, projectName);
            metricExecutor.execute();

            System.out.printf("Start analysing: %s\n", projectName);
            System.out.println(metricExecutor);
            new JsonOutput().writeResults(projectName, metricExecutor);
            out.println("Finished");
        }
    }

    private static List<String> getRepositoryUris() {
        // https://medium.com/issuehunt/50-top-java-projects-on-github-adbfe9f67dbc
        return Arrays.asList(
            "https://github.com/square/retrofit.git",
            //"https://github.com/elastic/elasticsearch.git",
            "https://github.com/square/okhttp.git",
            "https://github.com/google/guava.git",
            "https://github.com/PhilJay/MPAndroidChart.git",
            "https://github.com/bumptech/glide.git",
            //"https://github.com/spring-projects/spring-framework.git",
            "https://github.com/JakeWharton/butterknife.git",
            "https://github.com/airbnb/lottie-android.git",
            "https://github.com/square/leakcanary.git",
            "https://github.com/apache/dubbo.git",
            "https://github.com/zxing/zxing.git",
            "https://github.com/greenrobot/EventBus.git",
            "https://github.com/Blankj/AndroidUtilCode.git",
            "https://github.com/nostra13/Android-Universal-Image-Loader.git",
            "https://github.com/square/picasso.git",
            "https://github.com/skylot/jadx.git",
            "https://github.com/facebook/fresco.git",
            "https://github.com/netty/netty.git",
            "https://github.com/libgdx/libgdx.git",
            "https://github.com/Netflix/Hystrix.git",
            "https://github.com/alibaba/fastjson.git",
            "https://github.com/CymChad/BaseRecyclerViewAdapterHelper.git",
            "https://github.com/afollestad/material-dialogs.git",
            "https://github.com/Baseflow/PhotoView.git",
            "https://github.com/Tencent/tinker.git",
            "https://github.com/lgvalle/Material-Animations.git",
            //"https://github.com/nickbutcher/plaid",
            //"https://github.com/jfeinstein10/SlidingMenu.git",
            "https://github.com/jenkinsci/jenkins.git",
            "https://github.com/google/ExoPlayer.git",
            "https://github.com/greenrobot/greenDAO.git",
            "https://github.com/realm/realm-java.git",
            //"https://github.com/orhanobut/logger.git",
            "https://github.com/bazelbuild/bazel.git",
            "https://github.com/mybatis/mybatis-3.git",
            "https://github.com/square/dagger.git",
            "https://github.com/google/guice.git",
            "https://github.com/google/auto.git",
            "https://github.com/junit-team/junit4.git",
            "https://github.com/mockito/mockito.git",
            "https://github.com/square/javapoet.git",
            "https://github.com/OpenRefine/OpenRefine.git",
            "https://github.com/google/j2objc.git",
            "https://github.com/facebookarchive/rebound.git",
            "https://github.com/scribejava/scribejava.git",
            "https://github.com/square/moshi.git",
            "https://github.com/socketio/socket.io-client-java.git",
            "https://github.com/spring-projects/spring-boot.git",
            "https://github.com/eclipse/jetty.project.git",
            "https://github.com/apache/doris.git",
            "https://github.com/checkstyle/checkstyle.git",
            //"https://github.com/opensearch-project/OpenSearch.git",
            "https://github.com/testcontainers/testcontainers-java.git",
            "https://github.com/lax1dude/eaglercraftx-1.8.git",
            "https://github.com/apache/pinot.git",
            "https://github.com/alibaba/spring-cloud-alibaba.git",
            "https://github.com/apache/hive.git",
            "https://github.com/TheoKanning/openai-java.git",
            "https://github.com/apache/rocketmq.git",
            //"https://github.com/apache/flink.git",
            "https://github.com/apache/incubator-seatunnel.git",
            "https://github.com/apache/kafka.git",
            "https://github.com/apache/dolphinscheduler.git",
            "https://github.com/dromara/ChatGPT.git",
            "https://github.com/ReactiveX/RxJava.git");
    }

    private static String formatUriToProjectName(final String uri) {
        return uri
            .replaceFirst("https://github\\.com/", "")
            .replaceFirst("\\.git", "")
            .replace(".", "-")
            .replace("/", "-");
    }

    //final List<String> failingRepositoryUris =
    //    Arrays.asList(//"https://github.com/elastic/elasticsearch.git",
    //                  "https://github.com/kunal-kushwaha/DSA-Bootcamp-Java.git");
}
