package threadAB;

public class ThreadB extends Thread {

    private final ThreadA threadA;

    public ThreadB(ThreadA threadA) {
        this.threadA = threadA;
    }

    @Override
    public void run() {
        synchronized (threadA) {
            while (threadA.getStatus() != 's') {
                try {
                    threadA.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        long finishTime = System.currentTimeMillis() + 100;

        while (threadA.getStatus() != 'z') {
            System.out.println(finishTime - System.currentTimeMillis());
        }
    }
}
