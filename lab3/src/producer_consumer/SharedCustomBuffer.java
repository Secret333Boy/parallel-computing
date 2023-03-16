package producer_consumer;

public class SharedCustomBuffer<T> implements SharedBuffer<T> {
    @Override
    public T take() throws InterruptedException {
        return null;
    }

    @Override
    public void put(T object) throws InterruptedException {

    }
}
