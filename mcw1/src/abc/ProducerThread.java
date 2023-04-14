package abc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerThread extends Thread {
    private final ArrayBlockingQueue<Object> sharedBuffer;

    private final int MAX_VALUE = 100000;

    public ProducerThread(ArrayBlockingQueue<Object> sharedBuffer) {
        this.sharedBuffer = sharedBuffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object object = new Object();
                sharedBuffer.put(object);
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date now = new Date();
                System.out.println("[" + sdfDate.format(now) + "]" + " [PRODUCER] Sent message: " + object);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
