import java.util.*;
import java.util.concurrent.RecursiveTask;

public class WordLengthCharacteristicsTask extends RecursiveTask<OccasionalCharacteristics> {
    private static final int THRESHOLD = 10;

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

            int Mx = 0;
            int Mx2 = 0;

            for (WordLengthCharacteristicsTask task : invokeAll(dividedTasks)) {
                OccasionalCharacteristics result = task.join();

                Mx += result.getMathematicalExpectation();
                Mx2 += result.getMx2();
            }
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

        Map<Integer, Double> possibilityMap = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            possibilityMap.put(entry.getKey(), (double) entry.getValue() / (double) words.length);
        }

        double Mx = 0;
        double Mx2 = 0;

        for (Map.Entry<Integer, Double> entry : possibilityMap.entrySet()) {
            Mx += entry.getKey() * entry.getValue();
            Mx2 += Math.pow(entry.getKey(), 2) * entry.getValue();
        }

        return new OccasionalCharacteristics(Mx, Mx2);
    }
}
