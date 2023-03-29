package metric.execution;

public class MetricExecutorThread extends Thread {

    final MetricExecutor metricExecutor;

    public MetricExecutorThread(final MetricExecutor metricExecutor) {
        this.metricExecutor = metricExecutor;
    }

    @Override
    public void run() {
        super.run();
        metricExecutor.execute();
    }

    public MetricExecutor getMetricExecutor() {
        return metricExecutor;
    }
}
