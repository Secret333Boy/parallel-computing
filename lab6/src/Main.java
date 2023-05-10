import mpi.MPI;

public class Main {
    public static void main(String[] args) {
//        BlockingMPI blockingMPI = new BlockingMPI();
//        blockingMPI.runMatrixMultiplication(args);

        NonBlockingMPI nonBlockingMPI = new NonBlockingMPI();
        nonBlockingMPI.runMatrixMultiplication(args);
    }
}