import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();
        ReentrantLock locker = new ReentrantLock();

        IncrementThread incrementThread = new IncrementThread(counter, locker);
        DecrementThread decrementThread = new DecrementThread(counter, locker);

        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
            decrementThread.join();
            System.out.println(counter.getC());
        } catch (InterruptedException ignored) {

        }
    }
}