public class DecrementThread extends Thread {
    private final Counter counter;

    public DecrementThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100_000_000; i++) {
            this.counter.decrement();
        }
    }
}
