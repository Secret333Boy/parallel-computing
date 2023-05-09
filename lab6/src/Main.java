import mpi.MPI;

public class Main {
    public static void main(String[] args) {
        int matrixSize = 10;

        int[] rows = new int[1];
        int[] offset = new int[1];

        int numworkers, source, dest, /* rows of matrix A sent to each worker */
                averow, extra = 0, i, j, k, rc;
        double[][] a = new double[matrixSize][matrixSize]; /* matrix A to be multiplied */
        double[][] b = new double[matrixSize][matrixSize]; /* matrix B to be multiplied */
        double[][] c = new double[matrixSize][matrixSize]; /* result matrix C */

        int recv_buf = 0;
        int send_buf = 101;

        MPI.Init(args);
        int numtasks = MPI.COMM_WORLD.Size();
        int taskid = MPI.COMM_WORLD.Rank();
        if (numtasks < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...\n");
            System.exit(1);
        }
        numworkers = numtasks - 1;
        if (taskid == 0) {
            System.out.printf("mpi_mm has started with %d tasks.\n", numtasks);
            for (i = 0; i < matrixSize; i++)
                for (j = 0; j < matrixSize; j++)
                    a[i][j] = 10;
            for (i = 0; i < matrixSize; i++)
                for (j = 0; j < matrixSize; j++)
                    b[i][j] = 10;

            averow = matrixSize / numworkers;
            extra = matrixSize % numworkers;
            for (dest = 1; dest <= numworkers; dest++) {
                rows[0] = (dest <= extra) ? averow + 1 : averow;
                System.out.printf("Sending %d rows to task %d offset= %d\n", rows[0], dest, offset[0]);
                MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, dest, 1);
                MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, dest, 1);
                MPI.COMM_WORLD.Send(a, offset[0], rows[0], MPI.OBJECT, dest, 1);
                MPI.COMM_WORLD.Send(b, 0, matrixSize, MPI.OBJECT, dest, 1);

                offset[0] = offset[0] + rows[0];
            }
            /* Receive results from worker tasks */
            for (source = 1; source <= numworkers; source++) {
                MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, source, 2);
                MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, source, 2);
                MPI.COMM_WORLD.Recv(c, offset[0], rows[0], MPI.OBJECT, source, 2);

                System.out.printf("Received results from task %d\n", source);
            }
            /* Print results */
            System.out.print("****\n");
            System.out.print("Result Matrix:\n");
            for (i = 0; i < matrixSize; i++) {
                System.out.print("\n");
                for (j = 0; j < matrixSize; j++)
                    System.out.printf("%6.2f ", c[i][j]);
            }
            System.out.print("\n********\n");
            System.out.print("Done.\n");
        }
/******** worker task *****************/
        else { /* if (taskid > MASTER) */
            MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, 0, 1);
            MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, 0, 1);
            MPI.COMM_WORLD.Recv(a, 0, rows[0], MPI.OBJECT, 0, 1);
            MPI.COMM_WORLD.Recv(b, 0, matrixSize, MPI.OBJECT, 0, 1);

            for (k = 0; k < matrixSize; k++)
                for (i = 0; i < rows[0]; i++) {
                    c[i][k] = 0.0;
                    for (j = 0; j < matrixSize; j++)
                        c[i][k] = c[i][k] + a[i][j] * b[j][k];
                }

            MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, 0, 2);
            MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, 0, 2);
            MPI.COMM_WORLD.Send(c, 0, rows[0], MPI.OBJECT, 0, 2);
        }
        MPI.Finalize();
    }
}