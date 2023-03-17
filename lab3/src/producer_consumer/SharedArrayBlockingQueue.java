package producer_consumer;

import java.util.concurrent.ArrayBlockingQueue;

public class SharedArrayBlockingQueue<T> implements SharedBuffer<T> {

    private final ArrayBlockingQueue<T> queue;
    private int maximumSize = 0;

    public SharedArrayBlockingQueue(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    public T take() throws InterruptedException {
        return queue.take();
    }

    public void put(T object) throws InterruptedException {
        queue.put(object);
        synchronized (this) {
            int size = queue.size();
            if (size > maximumSize) maximumSize = size;
        }
    }

    public synchronized int getMaximumSize() {
        return this.maximumSize;
    }
}
