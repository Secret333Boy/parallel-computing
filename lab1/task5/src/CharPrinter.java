public class CharPrinter {
    private char lastCharPrinted;
    private int printedChars = 0;
    public void print(char c) {
        System.out.print(c);
        lastCharPrinted = c;
        printedChars++;
        if (printedChars == 100) {
            System.out.print('\n');
            printedChars = 0;
        }
    }

    public char getLastCharPrinted() {
        return lastCharPrinted;
    }
}
