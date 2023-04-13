import java.util.concurrent.Callable;

public class FoxCallable implements Callable<IntegerMatrix> {
    private final IntegerMatrix[] row;
    private final IntegerMatrix[] column;

    public FoxCallable(IntegerMatrix[] row, IntegerMatrix[] column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public IntegerMatrix call() {
        int blockSize = row[0].getWidth();

        IntegerMatrix ij = IntegerMatrix.getZeroMatrix(blockSize, blockSize);

        for (int k = 0; k < row.length; k++) {
            ij = ij.add(row[k].multiplySingleThread(column[k]).getMatrix());
        }

        return ij;
    }
}
