import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int parallelTests = 3;
        int channelsCount = 2;
        int queueCapacity = 100;
        int iterations = 1000;
        int maxProcessTime = 100;
        int maxTimeBeforeNextServe = 20;

        ExecutorService executorService = Executors.newFixedThreadPool(parallelTests);

        List<Future<ServeSystemTestResult>> resultFutureList = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < parallelTests; i++) {
            resultFutureList.add(executorService.submit(new ServeSystemTestTask(channelsCount, queueCapacity, iterations, maxProcessTime, maxTimeBeforeNextServe)));
        }

        executorService.shutdown();

        double averageQueueSizeSum = 0;
        double failureProbabilitySum = 0;

        for (Future<ServeSystemTestResult> resultFuture : resultFutureList) {
            ServeSystemTestResult serveSystemTestResult = resultFuture.get();
            averageQueueSizeSum += serveSystemTestResult.averageQueueLength();
            failureProbabilitySum += serveSystemTestResult.failureProbability();
        }

        long endTime = System.currentTimeMillis();


        double averageQueueSize = averageQueueSizeSum / parallelTests;
        double failureProbability = failureProbabilitySum / parallelTests;

        System.out.println("Final average queue size: " + averageQueueSize + " (" + averageQueueSize * 100 / queueCapacity + "%)");
        System.out.println("Final failure probability: " + failureProbability * 100 + "%");
        System.out.println("Total elapsed: " + (endTime - startTime) + "ms");
    }
}