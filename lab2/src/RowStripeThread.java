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
        synchronized (columnStripeClock) {
            columnStripeClock.register();

            int[] previousIterationColumn = null;

            for (int k = 0; k < row.length; k++) {
                int[] column = columnStripeClock.getColumn();

                while (column == previousIterationColumn) {
                    try {
                        columnStripeClock.wait();
                        column = columnStripeClock.getColumn();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                previousIterationColumn = column;

                int newValue = 0;
                for (int i = 0; i < row.length; i++) {
                    newValue += row[i] * column[i];
                }

                matrixRowStripeSetter.set(k + y >= row.length ? k + y - row.length : k + y, y, newValue);
                columnStripeClock.tick();
            }
        }
    }
}
