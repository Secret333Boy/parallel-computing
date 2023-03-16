package producer_consumer;

import java.util.concurrent.ArrayBlockingQueue;

public class SharedArrayBlockingQueue<T> implements SharedBuffer<T> {

    private final ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(100);

    public T take() throws InterruptedException {
        return queue.take();
    }

    public void put(T object) throws InterruptedException {
        queue.put(object);
    }
}
