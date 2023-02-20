public class SynchronizedCounter extends Counter {
    @Override
    public synchronized void increment() {
        super.increment();
    }

    @Override
    public synchronized void decrement() {
        super.decrement();
    }
}
