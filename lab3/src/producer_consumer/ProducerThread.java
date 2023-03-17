package producer_consumer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProducerThread extends Thread {
    private final SharedBuffer<Integer> sharedBuffer;

    private final int MAX_VALUE = 100000;

    public ProducerThread(SharedBuffer<Integer> sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int value = Math.toIntExact(Math.round(Math.random() * MAX_VALUE));
                sharedBuffer.put(value);
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date now = new Date();
                System.out.println("[" + sdfDate.format(now) + "]" + " [PRODUCER] Sent message: " + value + ". Shared buffer maximum size: " + sharedBuffer.getMaximumSize());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
