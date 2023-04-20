package threadAB;

public class ThreadA extends Thread {

    private char status = 's';
    private boolean active = true;

    @Override
    public void run() {
        while (active) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                status = status == 'z' ? 's' : 'z';
                notifyAll();
            }
        }
    }

    public synchronized char getStatus() {
        return status;
    }

    public synchronized void terminate() {
        this.active = false;
    }
}
