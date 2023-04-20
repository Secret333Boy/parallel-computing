package abc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public class ConsumerThread extends Thread {

    private final ArrayBlockingQueue<Object> sharedBuffer;

    public ConsumerThread(ArrayBlockingQueue<Object> sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object value = sharedBuffer.take();
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date now = new Date();
                System.out.println("[" + sdfDate.format(now) + "]" + " [CONSUMER] Got message: " + value);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
