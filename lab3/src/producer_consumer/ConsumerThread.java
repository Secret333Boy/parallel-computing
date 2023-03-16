package producer_consumer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsumerThread extends Thread {

    private final SharedBuffer<Integer> sharedBuffer;

    public ConsumerThread(SharedBuffer<Integer> sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int value = sharedBuffer.take();
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date now = new Date();
                System.out.println("[" + sdfDate.format(now) + "]" + " [CONSUMER] Got message: " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
