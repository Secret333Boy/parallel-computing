package producer_consumer;

import java.util.ArrayList;
import java.util.List;

public class SharedCustomBuffer<T> implements SharedBuffer<T> {
    private final List<T> list = new ArrayList<>();
    private final int capacity;
    private int maximumSize = 0;

    public SharedCustomBuffer(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public synchronized T take() throws InterruptedException {
        while (this.isEmpty()) {
            wait();
        }

        T object = this.list.get(0);
        this.list.remove(0);
        notifyAll();
        return object;
    }

    @Override
    public synchronized void put(T object) throws InterruptedException {
        while (this.isFull()) {
            wait();
        }

        this.list.add(object);
        int size = this.list.size();
        if (size > maximumSize) maximumSize = size;
        notifyAll();
    }

    private synchronized boolean isEmpty() {
        return this.list.size() == 0;
    }

    private synchronized boolean isFull() {
        return this.list.size() == capacity;
    }

    public synchronized int getMaximumSize() {
        return this.maximumSize;
    }
}
