package threadAB;

public class Main {
    public static void main(String[] args) {
        ThreadA threadA = new ThreadA();
        ThreadB threadB = new ThreadB(threadA);

        threadA.start();
        threadB.start();

        try {
            threadB.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadA.terminate();
        }
    }
}
