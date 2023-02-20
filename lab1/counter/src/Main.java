public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();

        IncrementThread incrementThread = new IncrementThread(counter);
        DecrementThread decrementThread = new DecrementThread(counter);

        incrementThread.start();
        decrementThread.start();

        while (incrementThread.isAlive() || decrementThread.isAlive()) {

        }
        System.out.println(counter.getC());
    }
}