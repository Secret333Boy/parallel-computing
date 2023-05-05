import java.util.concurrent.BlockingQueue;

public class Channel implements Runnable {

    private final BlockingQueue<Integer> queue;
    private boolean terminated = false;
    private boolean isBusy = false;

    public Channel(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!terminated) {
            try {
                int time = queue.take();
                this.isBusy = true;
                Thread.sleep(time);
                this.isBusy = false;
                synchronized (queue) {
                    queue.notifyAll();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void terminate() {
        this.terminated = true;
    }

    public boolean isBusy() {
        return isBusy;
    }
}
