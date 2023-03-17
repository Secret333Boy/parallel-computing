package producer_consumer;

public class Main {
    public static void main(String[] args) {
        SharedArrayBlockingQueue<Integer> sharedBuffer = new SharedArrayBlockingQueue<>(100);
//        SharedCustomBuffer<Integer> sharedBuffer = new SharedCustomBuffer<>(100);

        ProducerThread producerThread = new ProducerThread(sharedBuffer);
        ConsumerThread consumerThread = new ConsumerThread(sharedBuffer);

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

