import java.util.concurrent.locks.ReentrantLock;

public class DecrementThread extends Thread {
    private final Counter counter;
    private final ReentrantLock locker;

    public DecrementThread(Counter counter, ReentrantLock locker) {
        this.counter = counter;
        this.locker = locker;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100_000_000; i++) {
            locker.lock();
            try {
                this.counter.decrement();
            } finally {
                locker.unlock();
            }
        }
    }
}
