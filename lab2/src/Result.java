public class Result {
    private final IntegerMatrix integerMatrix;
    private final long elapsedTime;

    public Result(IntegerMatrix integerMatrix, long elapsedTime) {
        this.integerMatrix = integerMatrix;
        this.elapsedTime = elapsedTime;
    }

    public void printMatrix() {
        System.out.println(integerMatrix);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public boolean compareTo(Result result) {
        return integerMatrix.hasSameElements(result.integerMatrix);
    }

    public IntegerMatrix getMatrix() {
        return this.integerMatrix;
    }
}
