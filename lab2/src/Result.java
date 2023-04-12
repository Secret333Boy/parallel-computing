public class Result {
    private final Matrix matrix;
    private final long elapsedTime;

    public Result(Matrix matrix, long elapsedTime) {
        this.matrix = matrix;
        this.elapsedTime = elapsedTime;
    }

    public void printMatrix() {
        System.out.println(matrix);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public boolean compareTo(Result result) {
        return matrix.hasSameElements(result.matrix);
    }
}
