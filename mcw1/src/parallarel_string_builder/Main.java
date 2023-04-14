package parallarel_string_builder;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>(List.of(new String[]{"1", "4", "7"}));
        List<String> list2 = new ArrayList<>(List.of(new String[]{"2", "5", "8"}));
        List<String> list3 = new ArrayList<>(List.of(new String[]{"3", "6", "9"}));

        ParallelStringBuilder parallelStringBuilder = new ParallelStringBuilder();
        ThreadClock threadClock = new ThreadClock();

        StringListThread stringListThread1 = new StringListThread(list1, parallelStringBuilder, threadClock);
        StringListThread stringListThread2 = new StringListThread(list2, parallelStringBuilder, threadClock);
        StringListThread stringListThread3 = new StringListThread(list3, parallelStringBuilder, threadClock);

        threadClock.register(stringListThread1);
        threadClock.register(stringListThread2);
        threadClock.register(stringListThread3);

        stringListThread1.start();
        stringListThread2.start();
        stringListThread3.start();

        try {
            stringListThread1.join();
            stringListThread2.join();
            stringListThread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(parallelStringBuilder);
    }
}