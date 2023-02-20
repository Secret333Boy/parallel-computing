public class Main {
    public static void main(String[] args) {
        CharPrinter charPrinter = new CharPrinter();
        CharThread verticalLineThread = new CharThread('|', charPrinter);
        CharThread dashThread = new CharThread('-', charPrinter);

        verticalLineThread.setPeerThread(dashThread);
        dashThread.setPeerThread(verticalLineThread);

        verticalLineThread.start();
        dashThread.start();
    }
}