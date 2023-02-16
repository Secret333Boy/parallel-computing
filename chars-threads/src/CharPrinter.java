public class CharPrinter {
    private char lastCharPrinted;
    public void print(char c) {
        System.out.print(c);
        lastCharPrinted = c;
    }

    public char getLastCharPrinted() {
        return lastCharPrinted;
    }
}
