import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int[][] array1 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
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

//        Main.testOnBigMatrix();
//        Main.testOnBigMatrix();
         Main.printAverageStats(1000, 10, 5);
    }

    public static void testOnBigMatrix() {
        IntegerMatrix integerMatrix1 = IntegerMatrix.randomMatrix(1000, 1000);
        IntegerMatrix integerMatrix2 = IntegerMatrix.randomMatrix(1000, 1000);

        Result singleThreadResult = integerMatrix1.multiplySingleThread(integerMatrix2);
        System.out.println("Single thread total execution time: " + (singleThreadResult.getElapsedTime()) + "ms");

        int threadsCount = 4;

        System.out.println("Threads: " + threadsCount);

        Result stripeResult = integerMatrix1.multiplyStripe(integerMatrix2, threadsCount);
        System.out.println("Stripe algorithm total execution time: " + (stripeResult.getElapsedTime()) + "ms");

        int blockSize = 10;
        System.out.println("Block size: " + blockSize);

        Result foxResult = integerMatrix1.multiplyFox(integerMatrix2, blockSize, threadsCount);
        System.out.println("Fox algorithm total execution time: " + (foxResult.getElapsedTime()) + "ms");

        Result forkJoinResult = integerMatrix1.multiplyForkJoin(integerMatrix2);
        System.out.println("Fork join algorithm total execution time: " + (forkJoinResult.getElapsedTime()) + "ms");

        System.out.println("Stripe algorithm OK: " + singleThreadResult.compareTo(stripeResult));
        System.out.println("Fox algorithm OK: " + singleThreadResult.compareTo(foxResult));
        System.out.println("Fork join algorithm OK: " + singleThreadResult.compareTo(forkJoinResult));
    }

    public static void printAverageStats(int matrixSize, int blockSize, int testsCount) {
        System.out.println("Matrix size: " + matrixSize);
        System.out.println("Fox block size: " + blockSize);

        List<Long> serialTimeList = new ArrayList<>();

        for (int i = 0; i < testsCount; i++) {
            IntegerMatrix integerMatrix1 = IntegerMatrix.randomMatrix(matrixSize, matrixSize);
            IntegerMatrix integerMatrix2 = IntegerMatrix.randomMatrix(matrixSize, matrixSize);

            Result serialThreadResult = integerMatrix1.multiplySingleThread(integerMatrix2);
            serialTimeList.add(serialThreadResult.getElapsedTime());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        float averageSerialTime = ((float) serialTimeList.stream().reduce(Long::sum).get()) / testsCount;
        System.out.printf("Single thread average execution time: %fs\n", averageSerialTime / 1000);

        for (int k = 1; k < 5; k++) {
            int threadsCount = k * 2;

            if (threadsCount == 6) continue;

            System.out.println("---");
            System.out.println("Threads: " + threadsCount);
            List<Long> stripeTimeList = new ArrayList<>();
            List<Long> foxTimeList = new ArrayList<>();
            List<Long> forkJoinTimeList = new ArrayList<>();

            for (int i = 0; i < testsCount; i++) {
                IntegerMatrix integerMatrix1 = IntegerMatrix.randomMatrix(matrixSize, matrixSize);
                IntegerMatrix integerMatrix2 = IntegerMatrix.randomMatrix(matrixSize, matrixSize);

                Result stripeResult = integerMatrix1.multiplyStripe(integerMatrix2, threadsCount);
                stripeTimeList.add(stripeResult.getElapsedTime());

                Result foxResult = integerMatrix1.multiplyFox(integerMatrix2, blockSize, threadsCount);
                foxTimeList.add(foxResult.getElapsedTime());

                Result forkJoinResult = integerMatrix1.multiplyForkJoin(integerMatrix2);
                forkJoinTimeList.add(forkJoinResult.getElapsedTime());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            float averageStripeTime = ((float) stripeTimeList.stream().reduce(Long::sum).get()) / testsCount;
            float averageFoxTime = ((float) foxTimeList.stream().reduce(Long::sum).get()) / testsCount;
            float averageForkJoinTime = ((float) forkJoinTimeList.stream().reduce(Long::sum).get()) / testsCount;

            System.out.printf("Stripe algorithm average execution time: %fs\n", averageStripeTime / 1000);
            System.out.printf("Fox algorithm average execution time: %fs\n", averageFoxTime / 1000);
            System.out.printf("Fork join algorithm average execution time: %fs\n", averageForkJoinTime / 1000);

            System.out.printf("Stripe speed up: %f\n", averageSerialTime / averageStripeTime);
            System.out.printf("Fox speed up: %f\n", averageSerialTime / averageFoxTime);
            System.out.printf("Fork join speed up: %f\n", averageSerialTime / averageForkJoinTime);
        }
    }
}
