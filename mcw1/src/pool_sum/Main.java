package pool_sum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        double[] array = new double[50_000];

        for (int i = 0; i < array.length; i++) {
            array[i] = Math.random() * 100;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Function<double[], Callable<Double>> generateSumTask = (double[] arr) -> () -> Arrays.stream(arr).sum();

        List<Future<Double>> futures = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            double[] arrayPart = Arrays.copyOfRange(array, i * 100, i * 100 + 100);
            Callable<Double> callable = generateSumTask.apply(arrayPart);
            futures.add(executorService.submit(callable));
        }

        executorService.shutdown();

        double sum = 0;

        for (Future<Double> future: futures) {
            sum += future.get();
        }
        System.out.println(sum);
    }
}
