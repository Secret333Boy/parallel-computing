import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] array1 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14,15, 16}};
        int[][] array2 = {{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}};
        IntegerMatrix matrix = new IntegerMatrix(array1);
//        IntegerMatrix matrix = new IntegerMatrix(array2);

//        System.out.println(matrix);
//
//        matrix.multiplySingleThread(matrix).printMatrix();
//        matrix.multiplyStripe(matrix).printMatrix();
//        matrix.multiplyFox(matrix, 2).printMatrix();
//        matrix.multiplyForkJoin(matrix, 2).printMatrix();
//
//        System.out.println(matrix.hasSameElements(matrix));

        Main.testOnBigMatrix();
    }

    public static void testOnBigMatrix() {
        IntegerMatrix integerMatrix1 = IntegerMatrix.randomMatrix(1500, 1500);
        IntegerMatrix integerMatrix2 = IntegerMatrix.randomMatrix(1500, 1500);

        Result singleThreadResult = integerMatrix1.multiplySingleThread(integerMatrix2);
        System.out.println("Single thread total execution time: " + (singleThreadResult.getElapsedTime()) + "ms");

        Result stripeResult = integerMatrix1.multiplyStripe(integerMatrix2);
        System.out.println("Stripe algorithm total execution time: " + (stripeResult.getElapsedTime()) + "ms");

        Result foxResult = integerMatrix1.multiplyFox(integerMatrix2, 10);
        System.out.println("Fox algorithm total execution time: " + (foxResult.getElapsedTime()) + "ms");

        Result forkJoinResult = integerMatrix1.multiplyForkJoin(integerMatrix2);
        System.out.println("Fork join algorithm total execution time: " + (forkJoinResult.getElapsedTime()) + "ms");

        System.out.println("Stripe algorithm OK: " + singleThreadResult.compareTo(stripeResult));
        System.out.println("Fox algorithm OK: " + singleThreadResult.compareTo(foxResult));
        System.out.println("Fork join algorithm OK: " + singleThreadResult.compareTo(forkJoinResult));
    }
}
