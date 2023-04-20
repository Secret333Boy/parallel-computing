import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CommonWordsTask extends RecursiveTask<Set<String>> {
    private static final int THRESHOLD = 10;

    private final String[] words1;
    private final String[] words2;
    private final int start;
    private final int end;

    public CommonWordsTask(String[] words1, String[] words2, int start, int end) {
        this.words1 = words1;
        this.words2 = words2;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Set<String> compute() {
        if (end - start > THRESHOLD) {
            List<CommonWordsTask> dividedTasks = new ArrayList<>();
            int middleIndex = (start + end) / 2;
            dividedTasks.add(new CommonWordsTask(words1, words2, start, middleIndex));
            dividedTasks.add(new CommonWordsTask(words1, words2, middleIndex, end));

            return invokeAll(dividedTasks).stream().map(ForkJoinTask::join).reduce((acc, el) -> {
                acc.addAll(el);
                return acc;
            }).get();
        }

        Set<String> commonWords = new HashSet<>();

        for (int i = start; i < end; i++) {
            String word1 = words1[i];
            for (String word2 : words2) {
                if (Objects.equals(word1, word2)) commonWords.add(word1);
            }
        }

        return commonWords;
    }
}
