public class BlockMatrix {
    private final IntegerMatrix[][] array;

    public BlockMatrix(IntegerMatrix[][] array) {
        this.array = array;
    }

    public int getWidth() {
        return this.array[0].length;
    }

    public int getHeight() {
        return this.array.length;
    }

    public BlockMatrix add(BlockMatrix blockMatrix) {
        int n = this.getHeight();
        int m = this.getWidth();

        if (n != blockMatrix.getHeight() || m != blockMatrix.getWidth())
            throw new RuntimeException("Matrix has bad size");

        IntegerMatrix[][] newBlockIntegerMatrixArray = new IntegerMatrix[n][m];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                newBlockIntegerMatrixArray[j][i] = this.array[j][i].add(blockMatrix.array[j][i]);
            }
        }

        return new BlockMatrix(newBlockIntegerMatrixArray);
    }

    public BlockMatrix multiply(BlockMatrix blockMatrix) {
        int n = this.getHeight();
        int m = this.getWidth();

        if (m != blockMatrix.getHeight() || n != blockMatrix.getWidth())
            throw new RuntimeException("Matrix has bad size");

        IntegerMatrix[][] newArrayIntegerMatrices = new IntegerMatrix[n][m];

        int blockSize = this.getBlockSize();

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                IntegerMatrix ij = IntegerMatrix.getZeroMatrix(blockSize, blockSize);
                for (int k = 0; k < m; k++) {
                    ij = ij.add(this.array[j][k].multiplySingleThread(blockMatrix.array[k][i]).getMatrix());
                }
                newArrayIntegerMatrices[j][i] = ij;
            }
        }

        return new BlockMatrix(newArrayIntegerMatrices);
    }

    public int getBlockSize() {
        return this.array[0][0].getWidth();
    }

    public IntegerMatrix[][] getArray() {
        IntegerMatrix[][] arrayCopy = this.array.clone();

        for (int i = 0; i < arrayCopy.length; i++) {
            arrayCopy[i] = arrayCopy[i].clone();
        }

        return arrayCopy;
    }

    public BlockMatrix getTransponedMatrix() {
        int n = this.getHeight();
        int m = this.getWidth();

        IntegerMatrix[][] newMatrixArray = new IntegerMatrix[m][n];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                newMatrixArray[i][j] = this.array[j][i];
            }
        }

        return new BlockMatrix(newMatrixArray);
    }
}
