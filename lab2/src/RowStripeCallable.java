import java.util.concurrent.Callable;

public class RowStripeCallable implements Callable<int[]> {

    private final int[] row;

    private final ColumnStripeClock columnStripeClock;

    public RowStripeCallable(int[] row, ColumnStripeClock columnStripeClock) {
        this.row = row;
        this.columnStripeClock = columnStripeClock;
    }

    @Override
    public int[] call() throws Exception {
        int id = columnStripeClock.register();
        int[] resultRow = new int[row.length];

        int i = -1;
        for (int m = 0; m < row.length; m++) {
            i = columnStripeClock.getIndex(id);
            int[] column = columnStripeClock.getColumn(id);

            for (int k = 0; k < row.length; k++) {
                resultRow[i] += row[k] * column[k];
            }

            columnStripeClock.shift(id);
        }

        return resultRow;
    }
}
