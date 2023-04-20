import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class FindByKeyWordsTask extends RecursiveTask<List<String>> {
    private final String path;
    private final List<String> keyWords;
    private final CustomFileReader customFileReader = new CustomFileReader();

    public FindByKeyWordsTask(String path, List<String> keyWords) {
        this.path = path;
        this.keyWords = keyWords;
    }

    @Override
    protected List<String> compute() {
        File pathFile = new File(path);

        if (!pathFile.exists()) return new ArrayList<>();

        if (pathFile.isDirectory()) {
            File[] files = pathFile.listFiles();

            if (files == null) return new ArrayList<>();

            List<FindByKeyWordsTask> tasks = Arrays.stream(files).map((file) -> new FindByKeyWordsTask(file.getPath(), keyWords)).toList();

            return invokeAll(tasks).stream().map(ForkJoinTask::join).reduce((acc, el) -> {
                acc.addAll(el);
                return acc;
            }).orElseGet(ArrayList::new);
        }

        try {
            String text = customFileReader.readAll(path);

            for (String keyWord : keyWords) {
                if (text.matches(".*" + keyWord + ".*")) {
                    return new ArrayList<>(List.of(new String[]{path}));
                }
            }

        } catch (IOException ignored) {
            System.out.println();
        }

        return new ArrayList<>();
    }
}
