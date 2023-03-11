import java.util.ArrayList;
import java.util.List;

public class ColumnStripeClock {
    private final int[][] columns;
    private final int threadsToWait;
    private final List<Thread> registeredThreads = new ArrayList<>();
    private int tickedThreads = 0;
    private int finishedIterations = 0;

    public ColumnStripeClock(Matrix matrix) {
        int m = matrix.getWidth();
        int n = matrix.getHeight();
        int[][] columns = new int[m][n];
        for (int i = 0; i < m; i++) {
            columns[i] = matrix.getColumn(i);
        }

        this.columns = columns;
        this.threadsToWait = n;
    }

    public synchronized void register() {
        int size = registeredThreads.size();
        if (size == threadsToWait) throw new RuntimeException("Maximum registered threads");

        if (registeredThreads.contains(Thread.currentThread()))
            throw new RuntimeException("Thread is already registered");

        registeredThreads.add(Thread.currentThread());
    }

    public int getIndex() {
        return registeredThreads.indexOf(Thread.currentThread());
    }

    public int[] getColumn() {
        int index = registeredThreads.indexOf(Thread.currentThread());

        if (index < 0) throw new RuntimeException("Thread is not registered");

        return columns[index];
    }

    public synchronized void tick() {
        tickedThreads++;

        if (registeredThreads.size() != threadsToWait) return;

        if (tickedThreads == threadsToWait) {
            tickedThreads = 0;
        } else return;

        this.shift();

        finishedIterations++;
        this.notifyAll();
    }

    private synchronized void shift() {
        int size = registeredThreads.size();
        if (size == 0) throw new RuntimeException("Unable to shift: no threads were registered");
        Thread buf = registeredThreads.get(0);
        for (int i = 1; i < size; i++) {
            registeredThreads.set(i - 1, registeredThreads.get(i));
        }
        registeredThreads.set(size - 1, buf);
    }

    public boolean isFullCycle() {
        return finishedIterations == columns.length;
    }

    public List<Thread> getRegisteredThreads() {
        return registeredThreads;
    }
}
