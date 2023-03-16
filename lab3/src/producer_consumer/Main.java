package producer_consumer;

public class Main {
    public static void main(String[] args) {
        SharedArrayBlockingQueue<Integer> sharedArrayBlockingQueue = new SharedArrayBlockingQueue<>();

        ProducerThread producerThread = new ProducerThread(sharedArrayBlockingQueue);
        ConsumerThread consumerThread = new ConsumerThread(sharedArrayBlockingQueue);

        producerThread.start();
        consumerThread.start();
        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

