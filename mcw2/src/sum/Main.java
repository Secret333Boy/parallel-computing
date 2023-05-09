package sum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

class SumTask extends RecursiveTask<Double> {
    private final int THRESHOLD = 50;

    private final double[] arr;
    private final int start;
    private final int end;

    public SumTask(double[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Double compute() {
        if (end - start > THRESHOLD) {
            List<SumTask> tasks = new ArrayList<>();
            int middleIndex = (start + end) / 2;

            tasks.add(new SumTask(arr, start, middleIndex));
            tasks.add(new SumTask(arr, middleIndex, end));


            return invokeAll(tasks).stream().map(SumTask::join).reduce(Double::sum).orElse(0.0);
        }

        return Arrays.stream(arr).reduce(Double::sum).orElse(0.0);
    }
}
public class Main {
    public static void main(String[] args) {
        double[] arr = new double[]{1, 2.1, 3, 2};

        ForkJoinPool forkJoinPool = new ForkJoinPool(10);

        Future<Double> sumFuture = forkJoinPool.submit(new SumTask(arr, 0, arr.length));

        try {
            System.out.println(sumFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        forkJoinPool.shutdown();
    }
}
