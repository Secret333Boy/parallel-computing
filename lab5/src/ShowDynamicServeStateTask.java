public class ShowDynamicServeStateTask implements Runnable {
    private final ServeSystem serveSystem;
    private final int timeBetweenLogs;

    public ShowDynamicServeStateTask(ServeSystem serveSystem, int timeBetweenLogs) {
        this.serveSystem = serveSystem;
        this.timeBetweenLogs = timeBetweenLogs;
    }

    @Override
    public void run() {
        while (serveSystem.isStarted()) {
            synchronized (System.out) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("Queue size: " + serveSystem.getQueueSize() + " (" + serveSystem.getQueueSize() * 100 / serveSystem.queueCapacity() + "%)");
                System.out.println("Channels busy: " + serveSystem.countBusyChannels() + " (" + (double) serveSystem.countBusyChannels() * 100 / (double) serveSystem.totalChannels() + "%)");
                System.out.println();
            }

            try {
                Thread.sleep(timeBetweenLogs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
