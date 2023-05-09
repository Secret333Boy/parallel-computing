package wordlength;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

class WorldLengthSumTask extends RecursiveTask<Integer> {
    private final int THRESHOLD = 50;
    private final List<String> words;

    public WorldLengthSumTask(List<String> words) {
        this.words = words;
    }

    @Override
    protected Integer compute() {
        if (words.size() > THRESHOLD) {
            List<WorldLengthSumTask> tasks = new ArrayList<>();

            int middleIndex = words.size() / 2;

            tasks.add(new WorldLengthSumTask(words.subList(0, middleIndex)));
            tasks.add(new WorldLengthSumTask(words.subList(middleIndex, words.size())));


            return invokeAll(tasks).stream().map(WorldLengthSumTask::join).reduce(Integer::sum).orElse(0);
        }

        return words.stream().map(String::length).reduce(Integer::sum).orElse(0);
    }
}

public class Main {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>(List.of(new String[]{"afasf", "afaf", "a", "asfasfaf"}));

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

            Future<Integer> sumFuture = forkJoinPool.submit(new WorldLengthSumTask(words));

            try {
                double result = (double)sumFuture.get() / (double)words.size();
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            forkJoinPool.shutdown();
    }
}
