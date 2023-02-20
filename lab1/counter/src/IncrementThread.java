public class IncrementThread extends Thread {
    private final Counter counter;

    public IncrementThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100_000_000; i++) {
            this.counter.increment();
        }
    }
}
