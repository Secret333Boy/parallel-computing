import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ServeSystem serveSystem = new ServeSystem(2, 100);
        serveSystem.start();

        int unsuccessfulServes = 0;
        List<Integer> queueSizes = new ArrayList<>();

        int iterations = 1_000;

        for (int i = 0; i < iterations; i++) {
            int time = Math.toIntExact(Math.round(Math.random() * 100));

            boolean ok = serveSystem.serve(time);
            if (!ok) unsuccessfulServes++;

            queueSizes.add(serveSystem.getQueueSize());

            int timeUntilNextServe = Math.toIntExact(Math.round(Math.random() * 20));

            Thread.sleep(timeUntilNextServe);
        }
        serveSystem.stop();

        System.out.println("Unsuccessful serve probability: " + unsuccessfulServes * 100 / iterations + "%");
        System.out.println("Average queue size: " + (double) queueSizes.stream().reduce(Integer::sum).orElseThrow() / (double) iterations);
    }
}