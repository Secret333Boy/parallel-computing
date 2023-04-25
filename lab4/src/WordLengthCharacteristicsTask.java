import java.util.*;
import java.util.concurrent.RecursiveTask;

public class WordLengthCharacteristicsTask extends RecursiveTask<OccasionalCharacteristics> {
    private static final int THRESHOLD = 1000;

    private final String[] words;
    private final int start;
    private final int end;


    public WordLengthCharacteristicsTask(String[] words, int start, int end) {
        this.words = words;
        this.start = start;
        this.end = end;
    }

    @Override
    protected OccasionalCharacteristics compute() {
        if (end - start > THRESHOLD) {
            List<WordLengthCharacteristicsTask> dividedTasks = new ArrayList<>();
            int middleIndex = (start + end) / 2;
            dividedTasks.add(new WordLengthCharacteristicsTask(words, start, middleIndex));
            dividedTasks.add(new WordLengthCharacteristicsTask(words, middleIndex, end));

            double Mx = 0;
            double Mx2 = 0;

            for (WordLengthCharacteristicsTask task : invokeAll(dividedTasks)) {
                OccasionalCharacteristics result = task.join();

                Mx += result.getMathematicalExpectation();
                Mx2 += result.getMx2();
            }

            return new OccasionalCharacteristics(Mx, Mx2);
        }

        Map<Integer, Integer> countMap = new HashMap<>();

        for (int i = start; i < end; i++) {
            String word = words[i];

            int wordLength = word.length();

            if (!countMap.containsKey(wordLength)) {
                countMap.put(wordLength, 1);
                continue;
            }

            int count = countMap.get(wordLength);
            countMap.put(wordLength, count + 1);
        }

        double Mx = 0;
        double Mx2 = 0;

        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            double possibility = (double) entry.getValue() / (double) words.length;
            Mx += entry.getKey() * possibility;
            Mx2 += Math.pow(entry.getKey(), 2) * possibility;
        }

        return new OccasionalCharacteristics(Mx, Mx2);
    }
}
