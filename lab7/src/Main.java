import mpi.MPI;

public class Main {
    public static void main(String[] args) throws Exception {
//        OneToManyMPI oneToManyMPI = new OneToManyMPI();
//        oneToManyMPI.runMatrixMultiplication(args);

        ManyToManyMPI manyToManyMPI = new ManyToManyMPI();
        manyToManyMPI.runMatrixMultiplication(args);
    }
}