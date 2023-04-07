package metric.execution;

import java.util.Arrays;
import java.util.List;
public class MetricSettings {

    List<Metric> evaluatedMetrics;

    private MetricSettings() {
    }

    public static MetricSettings createIncluding(final List<Metric> includedMetrics) {
        final MetricSettings metricSettings = new MetricSettings();
        metricSettings.evaluatedMetrics = includedMetrics;
        return metricSettings;
    }

    public static MetricSettings createExcluding(List<Metric> excludedMetrics) {
        final MetricSettings metricSettings = createComplete();
        metricSettings.evaluatedMetrics.removeAll(excludedMetrics);
        return metricSettings;
    }

    public static MetricSettings createComplete() {
        final MetricSettings metricSettings = new MetricSettings();
        metricSettings.evaluatedMetrics = Arrays.asList(Metric.values());
        return metricSettings;
    }

    public boolean contains(final Metric metric) {
        return evaluatedMetrics.contains(metric);
    }
}
