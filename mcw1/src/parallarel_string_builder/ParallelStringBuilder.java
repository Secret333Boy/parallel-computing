package parallarel_string_builder;

import java.util.ArrayList;
import java.util.List;

public class ParallelStringBuilder {
    private final List<String> text = new ArrayList<>();

    public synchronized void addText(String string) {
        text.add(string);
    }

    public synchronized String toString() {
        return text.stream().reduce(String::concat).orElse(null);
    }
}
