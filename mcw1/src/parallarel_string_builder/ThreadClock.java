package parallarel_string_builder;

import java.util.ArrayList;
import java.util.List;

public class ThreadClock {
    private final List<Thread> threadList;
    private int clockIndex = 0;

    public ThreadClock() {
        this.threadList = new ArrayList<>();
    }

    public ThreadClock(List<Thread> threadList) {
        this.threadList = threadList;
    }

    public synchronized void register() {
        threadList.add(Thread.currentThread());
    }

    public synchronized void register(Thread thread) {
        threadList.add(thread);
    }

    public synchronized boolean myTurn() {
        return threadList.indexOf(Thread.currentThread()) == clockIndex;
    }

    public synchronized void tick() {
        clockIndex = clockIndex == threadList.size() - 1 ? 0 : clockIndex + 1;
    }
}
