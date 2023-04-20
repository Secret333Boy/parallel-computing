import java.util.concurrent.*;

public class IntegerMatrix {
    public static int MAX_RANDOM_VALUE = 1000;
    private final int[][] array;

    public IntegerMatrix(int[][] array) {
        this.array = array;
    }

    public static IntegerMatrix randomMatrix(int width, int height) {
        int[][] array = new int[height][width];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                array[j][i] = Math.toIntExact(Math.round(Math.random() * IntegerMatrix.MAX_RANDOM_VALUE));
            }
        }

        return new IntegerMatrix(array);
    }

    public static IntegerMatrix randomMatrix() {
        return IntegerMatrix.randomMatrix(500, 500);
    }

    public static IntegerMatrix getZeroMatrix(int width, int height) {
        int[][] array = new int[height][width];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                array[j][i] = 0;
            }
        }

        return new IntegerMatrix(array);
    }

    public static IntegerMatrix fromBlockMatrix(BlockMatrix blockMatrix) {
        int blockSize = blockMatrix.getBlockSize();

        int n = blockSize * blockMatrix.getHeight();
        int m = blockSize * blockMatrix.getWidth();

        IntegerMatrix[][] blockMatrixArray = blockMatrix.getArray();

        int[][] newIntegerMatrixArray = new int[n][m];

        for (int j = 0; j < n; j++) {
            int blockJ = Math.floorDiv(j, blockSize);
            for (int i = 0; i < m; i++) {
                int blockI = Math.floorDiv(i, blockSize);
                int[][] blockArray = blockMatrixArray[blockJ][blockI].getArray();

                newIntegerMatrixArray[j][i] = blockArray[j - blockJ * blockSize][i - blockI * blockSize];
            }
        }

        return new IntegerMatrix(newIntegerMatrixArray);
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

    public IntegerMatrix getTransponedMatrix() {
        int n = this.getHeight();
        int m = this.getWidth();

        int[][] newMatrixArray = new int[m][n];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                newMatrixArray[i][j] = this.array[j][i];
            }
        }

        return new IntegerMatrix(newMatrixArray);
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

    private boolean validateMatrixForProduct(IntegerMatrix integerMatrix) {
        return this.getWidth() == integerMatrix.getHeight() && this.getHeight() == integerMatrix.getWidth();
    }

    public IntegerMatrix add(IntegerMatrix integerMatrix) {
        int n = this.getHeight();
        int m = this.getWidth();

        if (n != integerMatrix.getHeight() || m != integerMatrix.getWidth()) throw new RuntimeException("Matrix has bad size");

        int[][] newMatrixArray = new int[n][m];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                newMatrixArray[j][i] = this.array[j][i] + integerMatrix.array[j][i];
            }
        }

        return new IntegerMatrix(newMatrixArray);
    }

    public Result multiplySingleThread(IntegerMatrix integerMatrix) {
        final long startTime = System.currentTimeMillis();

        if (!this.validateMatrixForProduct(integerMatrix)) throw new RuntimeException("Matrix has bad size");

        int n = this.getHeight();
        int m = this.getWidth();

        int[][] newArrayMatrix = new int[n][m];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                int ij = 0;
                for (int k = 0; k < m; k++) {
                    ij += this.getValue(k, j) * integerMatrix.getValue(i, k);
                }
                newArrayMatrix[j][i] = ij;
            }
        }

        IntegerMatrix resultIntegerMatrix = new IntegerMatrix(newArrayMatrix);

        final long endTime = System.currentTimeMillis();

        return new Result(resultIntegerMatrix, endTime - startTime);
    }

    public Result multiplyStripe(IntegerMatrix integerMatrix) {
        final long startTime = System.currentTimeMillis();

        if (!this.validateMatrixForProduct(integerMatrix)) throw new RuntimeException("Matrix has bad size");

        int n = this.getHeight();
        int m = this.getWidth();

        int threadsCount = Runtime.getRuntime().availableProcessors();

        ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);

        ColumnStripeClock columnStripeClock = new ColumnStripeClock(integerMatrix);

        Future<int[]>[] futures = new Future[n];
        for (int j = 0; j < n; j++) {
            Future<int[]> rowFuture = threadPool.submit(new RowStripeCallable(this.array[j], columnStripeClock));
            futures[j] = rowFuture;
        }

        int[][] newMatrixArray = new int[n][m];

        for (int i = 0; i < n; i++) {
            try {
                newMatrixArray[i] = futures[i].get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        threadPool.close();

        IntegerMatrix resultIntegerMatrix = new IntegerMatrix(newMatrixArray);

        final long endTime = System.currentTimeMillis();

        return new Result(resultIntegerMatrix, endTime - startTime);
    }

    public Result multiplyFox(IntegerMatrix integerMatrix, int blockSize) {
        final long startTime = System.currentTimeMillis();

        if (!this.validateMatrixForProduct(integerMatrix)) throw new RuntimeException("Matrix has bad size");

        BlockMatrix blockMatrixA = this.toBlockMatrix(blockSize);
        BlockMatrix blockMatrixB = integerMatrix.toBlockMatrix(blockSize);

        int n = blockMatrixA.getHeight();
        int m = blockMatrixA.getWidth();

        IntegerMatrix[][] blockMatrixARows = blockMatrixA.getArray();
        IntegerMatrix[][] blockMatrixBColumns = blockMatrixB.getTransponedMatrix().getArray();

        int threadsCount = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);

        Future<IntegerMatrix>[][] futureMatrixCArray = new Future[n][m];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                futureMatrixCArray[j][i] = threadPool.submit(new FoxCallable(blockMatrixARows[j], blockMatrixBColumns[i]));
            }
        }

        IntegerMatrix[][] blockMatrixCArray = new IntegerMatrix[n][m];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                try {
                    blockMatrixCArray[j][i] = futureMatrixCArray[j][i].get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        threadPool.close();

        IntegerMatrix resultIntegerMatrix = IntegerMatrix.fromBlockMatrix(new BlockMatrix(blockMatrixCArray));

        final long endTime = System.currentTimeMillis();

        return new Result(resultIntegerMatrix, endTime - startTime);
    }

    public Result multiplyForkJoin(IntegerMatrix integerMatrix) {
        final long startTime = System.currentTimeMillis();

        if (!this.validateMatrixForProduct(integerMatrix)) throw new RuntimeException("Matrix has bad size");

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        Future<IntegerMatrix> integerMatrixFuture = forkJoinPool.submit(new MultiplyTask(this.array, integerMatrix.getTransponedMatrix().array, 0, this.array.length));

        forkJoinPool.shutdown();

        try {
            IntegerMatrix matrix = integerMatrixFuture.get();
            final long endTime = System.currentTimeMillis();
            return new Result(matrix, endTime - startTime);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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

    public boolean hasSameElements(IntegerMatrix integerMatrix) {
        for (int j = 0; j < this.getHeight(); j++) {
            for (int i = 0; i < this.getWidth(); i++) {
                if (this.array[j][i] != integerMatrix.getValue(i, j)) return false;
            }
        }

        return true;
    }

    public BlockMatrix toBlockMatrix(int blockSize) {
        int n = this.getHeight();
        int m = this.getWidth();

        if (n % blockSize != 0 || m % blockSize != 0) throw new RuntimeException("Invalid block size");

        int newBlockMatrixArrayN = n / blockSize;
        int newBlockMatrixArrayM = m / blockSize;


        IntegerMatrix[][] newBlockMatrixArray = new IntegerMatrix[newBlockMatrixArrayN][newBlockMatrixArrayM];

        for (int j = 0; j < newBlockMatrixArrayN; j++) {
            for (int i = 0; i < newBlockMatrixArrayM; i++) {
                int[][] matrixArray = new int[blockSize][blockSize];

                int startJ = j * blockSize;
                int endJ = (j + 1) * blockSize;
                int startI = i * blockSize;
                int endI = (i + 1) * blockSize;

                for (int k = startJ; k < endJ; k++) {
                    if (endI - startI >= 0)
                        System.arraycopy(this.array[k], startI, matrixArray[k - startJ], 0, endI - startI);
                }

                newBlockMatrixArray[j][i] = new IntegerMatrix(matrixArray);
            }
        }

        return new BlockMatrix(newBlockMatrixArray);
    }
}
