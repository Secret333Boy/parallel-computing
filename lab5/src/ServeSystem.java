import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServeSystem {
    private final int channelsCount;
    private final BlockingQueue<Integer> queue;
    private final List<Channel> channels = new ArrayList<>();
    private ExecutorService executorService;

    public ServeSystem(int channelsCount, int queueCapacity) {
        this.channelsCount = channelsCount;
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
    }

    public boolean serve(int time) {
        if (this.executorService == null) throw new RuntimeException("Serve system is already stopped");

        return this.queue.offer(time);
    }

    public ServeSystem start() {
        if (this.executorService != null) throw new RuntimeException("Serve system is already started");

        this.executorService = Executors.newFixedThreadPool(channelsCount);

        for (int i = 0; i < channelsCount; i++) {
            Channel channel = new Channel(queue);
            executorService.execute(channel);
            channels.add(channel);
        }

        return this;
    }

    public ServeSystem stop() {
        if (this.executorService == null) throw new RuntimeException("Serve system is already stopped");

        this.executorService.shutdown();

        for (Channel channel : channels) {
            channel.terminate();
        }

        channels.clear();

        this.executorService = null;
        return this;
    }

    public ServeSystem gracefulStop() {
        if (this.executorService == null) throw new RuntimeException("Serve system is already stopped");

        this.executorService.shutdown();
        this.executorService = null;

        synchronized (queue) {
            while (queue.size() != 0) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Channel channel : channels) {
            channel.terminate();
        }

        channels.clear();

        return this;
    }

    public ServeSystem restart() {
        return this.gracefulStop().start();
    }

    public int getQueueSize() {
        return this.queue.size();
    }
}
