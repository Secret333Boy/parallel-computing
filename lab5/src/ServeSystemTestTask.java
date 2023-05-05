import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServeSystemTestTask implements Callable<ServeSystemTestResult> {

    private final int channelsCount;
    private final int queueCapacity;
    private final int iterations;
    private final int maxProcessTime;
    private final int maxTimeBeforeNextServe;

    public ServeSystemTestTask(int channelsCount, int queueCapacity, int iterations, int maxProcessTime, int maxTimeBeforeNextServe) {
        this.channelsCount = channelsCount;
        this.queueCapacity = queueCapacity;
        this.iterations = iterations;
        this.maxProcessTime = maxProcessTime;
        this.maxTimeBeforeNextServe = maxTimeBeforeNextServe;
    }

    @Override
    public ServeSystemTestResult call() throws Exception {
        ServeSystem serveSystem = new ServeSystem(channelsCount, queueCapacity);
        serveSystem.start();

        int unsuccessfulServes = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Double> averageQueueSizeFuture = executorService.submit(new AverageQueueSizeTask(serveSystem));
        executorService.execute(new ShowDynamicServeStateTask(serveSystem, 1000));
        executorService.shutdown();

        for (int i = 0; i < iterations; i++) {
            int timeToProcess = Math.toIntExact(Math.round(Math.random() * maxProcessTime));

            boolean ok = serveSystem.serve(timeToProcess);
            if (!ok) unsuccessfulServes++;

            int timeUntilNextServe = Math.toIntExact(Math.round(Math.random() * maxTimeBeforeNextServe));

            Thread.sleep(timeUntilNextServe);
        }
        serveSystem.stop();

        double failureProbability = (double) unsuccessfulServes / (double) iterations;


        return new ServeSystemTestResult(averageQueueSizeFuture.get(), failureProbability);
    }
}
