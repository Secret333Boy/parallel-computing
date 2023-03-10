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

    public synchronized int[] getColumn() {
        int index = registeredThreads.indexOf(Thread.currentThread());

        if (index < 0) throw new RuntimeException("Thread is not registered");

        return columns[index];
    }

    public synchronized void tick() {
        tickedThreads++;

        if (tickedThreads == threadsToWait) {
            tickedThreads = 0;
        } else return;

        int[] buf = columns[0];
        for (int i = 1; i < columns.length; i++) {
            columns[i - 1] = columns[i];
        }
        columns[columns.length - 1] = buf;

        finishedIterations++;
        this.notifyAll();
    }

    public boolean isFullCycle() {
        return finishedIterations == columns.length;
    }
}
