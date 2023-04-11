import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ColumnStripeClock {
    private final int[][] columns;
    private final Map<Integer, Integer> idToIndexMap = new HashMap<>();

    public ColumnStripeClock(Matrix matrix) {
        this.columns = matrix.getTransponedMatrix().getArray();
    }

    public synchronized int register() {
        int id = idToIndexMap.size();
        idToIndexMap.put(id, id);
        return id;
    }

    public synchronized int getIndex(int id) throws Exception {
        int i = idToIndexMap.get(id);

        if (i < 0) throw new Exception("Unregistered value");
        return i;
    }

    public synchronized int[] getColumn(int id) throws Exception {
        return columns[this.getIndex(id)];
    }

    public synchronized void shift(int id) {
        int index = idToIndexMap.get(id);
        idToIndexMap.put(id, index == columns.length - 1 ? 0 : index + 1);
    }

//    public synchronized void register() {
//        int size = registeredThreads.size();
//        if (size == threadsToWait) throw new RuntimeException("Maximum registered threads");
//
//        if (registeredThreads.contains(Thread.currentThread()))
//            throw new RuntimeException("Thread is already registered");
//
//        registeredThreads.add(Thread.currentThread());
//    }
//
//    public int getIndex() {
//        return registeredThreads.indexOf(Thread.currentThread());
//    }
//
//    public int[] getColumn() {
//        int index = registeredThreads.indexOf(Thread.currentThread());
//
//        if (index < 0) throw new RuntimeException("Thread is not registered");
//
//        return columns[index];
//    }
//
//    public synchronized void tick() {
//        tickedThreads++;
//
//        if (registeredThreads.size() != threadsToWait) return;
//
//        if (tickedThreads == threadsToWait) {
//            tickedThreads = 0;
//        } else return;
//
//        this.shift();
//
//        finishedIterations++;
//        this.notifyAll();
//    }
//
//    private synchronized void shift() {
//        int size = registeredThreads.size();
//        if (size == 0) throw new RuntimeException("Unable to shift: no threads were registered");
//        Thread buf = registeredThreads.get(0);
//        for (int i = 1; i < size; i++) {
//            registeredThreads.set(i - 1, registeredThreads.get(i));
//        }
//        registeredThreads.set(size - 1, buf);
//    }
//

//
//    public List<Thread> getRegisteredThreads() {
//        return registeredThreads;
//    }
}
