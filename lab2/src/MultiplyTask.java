import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MultiplyTask extends RecursiveTask<IntegerMatrix> {
    private static final int THRESHOLD = 20;
    private final int[][] rows;
    private final int[][] columns;
    private final int start;
    private final int end;


    public MultiplyTask(int[][] rows, int[][] columns, int start, int end) {
        this.rows = rows;
        this.columns = columns;
        this.start = start;
        this.end = end;
    }

    @Override
    protected IntegerMatrix compute() {
        if (end - start > THRESHOLD) {
            List<MultiplyTask> dividedTasks = new ArrayList<>();
            int middleIndex = (start + end) / 2;
            dividedTasks.add(new MultiplyTask(rows, columns, start, middleIndex));
            dividedTasks.add(new MultiplyTask(rows, columns, middleIndex, end));

            return invokeAll(dividedTasks).stream().map(ForkJoinTask::join).reduce((acc, el) -> {
                int[][] accArray = acc.getArray();
                int[][] elArray = el.getArray();

                int[][] newArray = new int[accArray.length + elArray.length][columns.length];

                System.arraycopy(accArray, 0, newArray, 0, accArray.length);
                System.arraycopy(elArray, 0, newArray, accArray.length, elArray.length);

                return new IntegerMatrix(newArray);
            }).orElseThrow();
        }

        int[][] multipliedRows = new int[end - start][columns.length];

        for (int j = start; j < end; j++) {
            for (int i = 0; i < columns.length; i++) {
                for (int k = 0; k < columns.length; k++) {
                    multipliedRows[j - start][i] += rows[j][k] * columns[i][k];
                }
            }
        }

        return new IntegerMatrix(multipliedRows);
    }
}
