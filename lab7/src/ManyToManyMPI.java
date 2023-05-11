import mpi.MPI;

import java.util.Arrays;

public class ManyToManyMPI {
    public void runMatrixMultiplication(String[] args) throws Exception {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (size < 2) {
            MPI.COMM_WORLD.Abort(1);
            throw new Exception("At least two nodes are expected");
        }

        int N = 500;
        int[][] A = new int[N][N];
        int[][] B = new int[N][N];
        int[][] C = new int[N][N];

        if (rank == 0) {
            System.out.println("Nodes: " + size);
            System.out.println("Matrix size: " + N);


            for (int j = 0; j < N; j++) {
                for (int i = 0; i < N; i++) {
                    A[j][i] = 10;
                    B[j][i] = 10;
                }
            }

//            for (int i = 0; i < N; i++) {
//                A[i][i] = 10;
//                B[i][i] = 10;
//            }
        }

        long startTime = System.currentTimeMillis();

        int rowsPerNode = N / size;
        int extraRows = N % size;

        MPI.COMM_WORLD.Bcast(B, 0, N, MPI.OBJECT, 0);

        MPI.COMM_WORLD.Scatter(A, 0, rowsPerNode, MPI.OBJECT, A, 0, rowsPerNode, MPI.OBJECT, 0);

        for (int i = 0; i < rowsPerNode; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        MPI.COMM_WORLD.Allgather(C, 0, rowsPerNode, MPI.OBJECT, C, 0, rowsPerNode, MPI.OBJECT);

        if (rank == 0) {
            for (int i = C.length - extraRows; i < C.length; i++) {
                for (int j = 0; j < N; j++) {
                    for (int k = 0; k < N; k++) {
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Elapsed: " + (endTime - startTime));
//            System.out.println("Matrix C:");
//            for (int i = 0; i < N; i++) {
//                for (int j = 0; j < N; j++) {
//                    System.out.print(C[i][j] + " ");
//                }
//                System.out.println();
//            }
        }

        MPI.Finalize();
    }
}
