public class RowStripeThread extends Thread {
    private final int[] row;
    private final int y;
    private final ColumnStripeClock columnStripeClock;
    private final MatrixRowStripeSetter matrixRowStripeSetter;

    public RowStripeThread(int[] row, int y, ColumnStripeClock columnStripeClock, MatrixRowStripeSetter matrixRowStripeSetter) {
        this.row = row;
        this.y = y;
        this.columnStripeClock = columnStripeClock;
        this.matrixRowStripeSetter = matrixRowStripeSetter;
    }

    @Override
    public void run() {
        columnStripeClock.register();

        int previousIterationIndex = -1;

        for (int k = 0; k < row.length; k++) {
            int index;
            synchronized (columnStripeClock) {
                index = columnStripeClock.getIndex();

                while (index == previousIterationIndex) {
                    try {
                        columnStripeClock.wait();
                        index = columnStripeClock.getIndex();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                previousIterationIndex = index;
            }

            int[] column = columnStripeClock.getColumn();

            int newValue = 0;
            for (int i = 0; i < row.length; i++) {
                newValue += row[i] * column[i];
            }

            matrixRowStripeSetter.set(index, y, newValue);
            columnStripeClock.tick();
        }
    }
}
