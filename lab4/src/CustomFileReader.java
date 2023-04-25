import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CustomFileReader {
    public String readAll(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));

        StringBuilder textBuilder = new StringBuilder();

        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            textBuilder.append(line).append("\n");
        }

        return textBuilder.toString();
    }
}
