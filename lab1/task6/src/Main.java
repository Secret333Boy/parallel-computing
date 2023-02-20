public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();

        IncrementThread incrementThread = new IncrementThread(counter);
        DecrementThread decrementThread = new DecrementThread(counter);

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