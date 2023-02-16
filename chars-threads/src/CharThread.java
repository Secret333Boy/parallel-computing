public class CharThread extends Thread {

    private final char threadChar;
    private final CharPrinter charPrinter;
    private CharThread peerThread;

    public CharThread(char threadChar, CharPrinter charPrinter) {
        this.threadChar = threadChar;
        this.charPrinter = charPrinter;
    }

    public void setPeerThread(CharThread thread) {
        this.peerThread = thread;
    }

    @Override
    public void run() {
        synchronized (this.charPrinter) {
            for (int i = 0; i < 100; i++) {
                while (this.charPrinter.getLastCharPrinted() == threadChar) {
                    try {
                        this.charPrinter.wait();
                    } catch (InterruptedException ignored) {

                    }
                }
                this.charPrinter.print(threadChar);
                this.charPrinter.notify();
            }
        }
    }
}
