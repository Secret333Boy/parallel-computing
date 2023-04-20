package abc;

import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static void main(String[] args) {
        ArrayBlockingQueue<Object> arrayBlockingQueue = new ArrayBlockingQueue<>(10);

        ProducerThread threadA = new ProducerThread(arrayBlockingQueue);
        ConsumerThread threadB = new ConsumerThread(arrayBlockingQueue);
        ConsumerThread threadC = new ConsumerThread(arrayBlockingQueue);

        threadA.start();
        threadB.start();
        threadC.start();

        try {
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
