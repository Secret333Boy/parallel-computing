package producer_consumer;

public interface SharedBuffer<T> {
    T take() throws InterruptedException;

    void put(T object) throws InterruptedException;
}
