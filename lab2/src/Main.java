public class Main {
    public static void main(String[] args) {
//        int[][] array = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14,15, 16}};
//        int[][] array = {{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}};
//        Matrix matrix = new Matrix(array);
//        System.out.println(matrix);
//
//        System.out.println(matrix.multiplySingleThread(matrix));
//
//        System.out.println(matrix.multiplyStripe(matrix));
//
//        System.out.println(matrix.hasSameElements(matrix));
        Main.testOnBigMatrix();
    }

    public static void testOnBigMatrix() {
        Matrix matrix1 = Matrix.randomMatrix(500, 500);
        Matrix matrix2 = Matrix.randomMatrix(500, 500);

        final long singleThreadTimeStart = System.currentTimeMillis();
        Matrix singleThreadResult = matrix1.multiplySingleThread(matrix2);
        final long singleThreadTimeEnd = System.currentTimeMillis();
        System.out.println("Single thread total execution time: " + (singleThreadTimeEnd - singleThreadTimeStart) + "ms");

        final long stripeThreadTimeStart = System.currentTimeMillis();
        Matrix stripeResult = matrix1.multiplyStripe(matrix2);
        final long stripeThreadTimeEnd = System.currentTimeMillis();
        System.out.println("Stripe algorithm total execution time: " + (stripeThreadTimeEnd - stripeThreadTimeStart) + "ms");

        System.out.println("Stripe algorithm OK: " + singleThreadResult.hasSameElements(stripeResult));
    }
}
