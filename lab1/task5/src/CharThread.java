public class CharThread extends Thread {

    private final char threadChar;
    private final CharPrinter charPrinter;

    public CharThread(char threadChar, CharPrinter charPrinter) {
        this.threadChar = threadChar;
        this.charPrinter = charPrinter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            this.charPrinter.print(threadChar);
        }
//        synchronized (this.charPrinter) {
//            for (int i = 0; i < 100; i++) {
//                while (this.charPrinter.getLastCharPrinted() == threadChar) {
//                    try {
//                        this.charPrinter.wait();
//                    } catch (InterruptedException ignored) {
//
//                    }
//                }
//                this.charPrinter.print(threadChar);
//                this.charPrinter.notify();
//            }
//        }
    }
}
