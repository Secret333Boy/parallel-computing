import java.util.ArrayList;
import java.util.List;

public class Matrix {
    public static int MAX_RANDOM_VALUE = 1000;
    private final int[][] array;

    public Matrix(int[][] array) {
        this.array = array;
    }

    public static Matrix randomMatrix(int width, int height) {
        int[][] array = new int[height][width];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                array[j][i] = Math.toIntExact(Math.round(Math.random() * Matrix.MAX_RANDOM_VALUE));
            }
        }

        return new Matrix(array);
    }

    public static Matrix randomMatrix() {
        return Matrix.randomMatrix(500, 500);
    }

    public int[][] getArray() {
        int[][] arrayCopy = this.array.clone();

        for (int i = 0; i < arrayCopy.length; i++) {
            arrayCopy[i] = arrayCopy[i].clone();
        }

        return arrayCopy;
    }

    public int[] getRow(int y) {
        return this.array[y].clone();
    }

    public int[] getColumn(int x) {
        int[] column = new int[this.array.length];

        for (int k = 0; k < this.array.length; k++) {
            column[k] = this.array[k][x];
        }

        return column;
    }

    public int getWidth() {
        return this.array[0].length;
    }

    public int getHeight() {
        return this.array.length;
    }

    public int getValue(int x, int y) {
        return this.array[y][x];
    }

    public void setValue(int x, int y, int value) {
        this.array[y][x] = value;
    }

    private boolean validateMatrixForProduct(Matrix matrix) {
        return this.getWidth() == matrix.getHeight() && this.getHeight() == matrix.getWidth();
    }

    public Matrix multiplySingleThread(Matrix matrix) {
        if (!this.validateMatrixForProduct(matrix)) throw new RuntimeException("Matrix has bad size");

        int n = this.getHeight();
        int m = this.getWidth();

        int[][] newArrayMatrix = new int[n][m];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                int ij = 0;
                for (int k = 0; k < m; k++) {
                    ij += this.getValue(k, j) * matrix.getValue(i, k);
                }
                newArrayMatrix[j][i] = ij;
            }
        }

        return new Matrix(newArrayMatrix);
    }

    public Matrix multiplyStripe(Matrix matrix) {
        if (!this.validateMatrixForProduct(matrix)) throw new RuntimeException("Matrix has bad size");

        int n = this.getHeight();
        int m = this.getWidth();

        int[][] newArrayMatrix = new int[n][m];
        ColumnStripeClock columnStripeClock = new ColumnStripeClock(matrix);

        List<RowStripeThread> threadList = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            RowStripeThread rowStripeThread = new RowStripeThread(this.array[j], j, columnStripeClock, (int x, int y, int value) -> newArrayMatrix[y][x] = value);
            rowStripeThread.setPriority(10);
            rowStripeThread.start();
            threadList.add(rowStripeThread);
        }

        for (RowStripeThread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return new Matrix(newArrayMatrix);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int maxWidth = getMaxElementWidth();

        for (int[] row : this.array) {
            for (int element : row) {
                sb.append(String.format("%" + maxWidth + "d ", element));
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    private int getMaxElementWidth() {
        int maxWidth = 0;

        for (int[] row : this.array) {
            for (int element : row) {
                int width = String.valueOf(element).length();
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }

        return maxWidth;
    }

    public boolean hasSameElements(Matrix matrix) {
        for (int j = 0; j < this.getHeight(); j++) {
            for (int i = 0; i < this.getWidth(); i++) {
                if (this.array[j][i] != matrix.getValue(i, j)) return false;
            }
        }

        return true;
    }
}
