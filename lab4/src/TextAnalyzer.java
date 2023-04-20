import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class TextAnalyzer {
    private final String wordSplitRegexp = "[ \n\t]+";

    public OccasionalCharacteristics getWordLengthCharacteristics(String text) {
        String[] words = text.split(wordSplitRegexp);

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        Future<OccasionalCharacteristics> occationalCharacteristicsFuture = forkJoinPool.submit(new WordLengthCharacteristicsTask(words, 0, words.length));

        forkJoinPool.shutdown();

        try {
            return occationalCharacteristicsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public OccasionalCharacteristics getWordLengthCharacteristicsSingleThread(String text) {
        String[] words = text.split(wordSplitRegexp);

        Map<Integer, Integer> countMap = new HashMap<>();

        for (String word : words) {
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

    public Set<String> findCommonWords(String text1, String text2) {
        String[] words1 = text1.split(wordSplitRegexp);
        String[] words2 = text2.split(wordSplitRegexp);

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        Future<Set<String>> commonWordsFuture = forkJoinPool.submit(new CommonWordsTask(words1, words2, 0, words1.length));

        forkJoinPool.shutdown();

        try {
            return commonWordsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> findCommonWordsSingleThread(String text1, String text2) {
        Set<String> commonWords = new HashSet<>();

        String[] words1 = text1.split(wordSplitRegexp);
        String[] words2 = text2.split(wordSplitRegexp);

        for (String word1 : words1) {
            for (String word2 : words2) {
                if (Objects.equals(word1, word2)) commonWords.add(word1);
            }
        }

        return commonWords;
    }

    public List<String> findInDirectoryByKeyWords(String path, List<String> keyWords) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Future<List<String>> foundFilesFuture = forkJoinPool.submit(new FindByKeyWordsTask(path, keyWords));

        forkJoinPool.shutdown();

        try {
            return foundFilesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
