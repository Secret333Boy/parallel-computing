import java.util.concurrent.BlockingQueue;

public class Channel implements Runnable {

    private final BlockingQueue<Integer> queue;
    private boolean terminated = false;

    public Channel(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!terminated) {
            try {
                int time = queue.take();
                Thread.sleep(time);
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
}
