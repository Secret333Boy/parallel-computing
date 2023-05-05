import java.util.concurrent.Callable;

public class AverageQueueSizeTask implements Callable<Double> {
    private final ServeSystem serveSystem;

    public AverageQueueSizeTask(ServeSystem serveSystem) {
        this.serveSystem = serveSystem;
    }

    @Override
    public Double call() throws InterruptedException {

        int queueSizeSum = 0;
        int iterations = 0;

        while (this.serveSystem.isStarted()) {
            queueSizeSum += this.serveSystem.getQueueSize();
            iterations++;
            Thread.sleep(100);
        }

        if (iterations == 0) {
            return 0.0;
        }


        return (double) queueSizeSum / (double) iterations;
    }
}
