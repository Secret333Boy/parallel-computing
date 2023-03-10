public class Counter {

    private int c = 0;

    public void increment() {
        synchronized(this) {
            c++;
        }
    }

    public synchronized void decrement() {
        c--;
    }

    public int getC() {
        return c;
    }
}
