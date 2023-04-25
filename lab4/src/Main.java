import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        Main.runTask1();
    }

    public static void runTask1() throws IOException {
        CustomFileReader customFileReader = new CustomFileReader();
        String filePath = "E:\\Projects\\parallel-computing\\lab4\\src\\files\\sherlock.txt";

        String text = customFileReader.readAll(filePath);

        TextAnalyzer textAnalyzer = new TextAnalyzer();

        final long singleThreadStartTime = System.currentTimeMillis();
        OccasionalCharacteristics occasionalCharacteristicsSingleThread = textAnalyzer.getWordLengthCharacteristicsSingleThread(text);
        final long singleThreadEndTime = System.currentTimeMillis();
        System.out.println("Single thread result:");
        System.out.println("Elapsed: " + (singleThreadEndTime - singleThreadStartTime) + "ms");
        System.out.println(occasionalCharacteristicsSingleThread);

        final long forkJoinStartTime = System.currentTimeMillis();
        OccasionalCharacteristics occasionalCharacteristicsForkJoin = textAnalyzer.getWordLengthCharacteristics(text);
        final long forkJoinEndTime = System.currentTimeMillis();
        System.out.println("Fork join result:");
        System.out.println("Elapsed: " + (forkJoinEndTime - forkJoinStartTime) + "ms");
        System.out.println(occasionalCharacteristicsForkJoin);
    }

    public static void runTask2() {
        // in lab2
    }

    public static void runTask3() throws IOException {
//        String text1 = "Hello Hello Hello  1";
//        String text2 = "Hello no 1";

        CustomFileReader customFileReader = new CustomFileReader();

        String text1 = customFileReader.readAll("E:\\Projects\\parallel-computing\\lab4\\src\\files\\sherlock.txt");
        String text2 = customFileReader.readAll("E:\\Projects\\parallel-computing\\lab4\\src\\files\\romeo_and_juliet.txt");

        TextAnalyzer textAnalyzer = new TextAnalyzer();

        Set<String> commonWords = textAnalyzer.findCommonWords(text1, text2);

        System.out.println(commonWords);
    }

    public static void runTask4() {
        TextAnalyzer textAnalyzer = new TextAnalyzer();

        List<String> keyWords = new ArrayList<>(List.of(new String[]{"love", "crime"}));

        List<String> files = textAnalyzer.findInDirectoryByKeyWords("E:\\Projects\\parallel-computing\\lab4\\src\\files", keyWords);

        System.out.println(files);
    }
}