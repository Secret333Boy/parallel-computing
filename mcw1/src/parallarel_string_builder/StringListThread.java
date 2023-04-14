package parallarel_string_builder;

import java.util.List;

public class StringListThread extends Thread {
    private final List<String> stringList;
    private final ParallelStringBuilder parallelStringBuilder;
    private final ThreadClock threadClock;

    public StringListThread(List<String> stringList, ParallelStringBuilder parallelStringBuilder, ThreadClock threadClock) {
        this.stringList = stringList;
        this.parallelStringBuilder = parallelStringBuilder;
        this.threadClock = threadClock;
    }

    @Override
    public void run() {
        while (!stringList.isEmpty()) {
            synchronized (parallelStringBuilder) {
                while (!threadClock.myTurn()) {
                    try {
                        parallelStringBuilder.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                parallelStringBuilder.addText(stringList.get(0));
                stringList.remove(0);
                threadClock.tick();
                parallelStringBuilder.notifyAll();
            }
        }
    }
}
