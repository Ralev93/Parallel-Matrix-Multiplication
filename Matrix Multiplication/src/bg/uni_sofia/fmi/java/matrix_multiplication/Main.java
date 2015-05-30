package bg.uni_sofia.fmi.java.matrix_multiplication;

import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;
import bg.uni_sofia.fmi.java.matrix_multiplication.linear.MatrixMultiplierLinear;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import bg.uni_sofia.fmi.java.matrix_multiplication.parallel.MatrixMultiplierParallel;

import java.io.File;
import java.io.IOException;

public class Main {

    private final static int ATTEMPTS = 1;
    private static final int k = 1;//Runtime.getRuntime().availableProcessors();
    private final static int totalIterations = ATTEMPTS + (2 * k) * ATTEMPTS;
    private final static String ERROR_MSG = "The columns of the first matrix must"
            + " be equal of the rows of the second!\nTerminating.";

    static void showProgress(int done) {
        System.out.printf("%.0f%% ", (float) done / totalIterations * 100);
    }

    public static void main(String[] args) throws IOException {
        Matrix left = new Matrix();
        left.readFromFile(new File( "TestData\\Ex2\\left"));

        Matrix right = new Matrix();
        right.readFromFile(new File("TestData\\Ex2\\right"));

        Matrix result = null;

        MatrixMultiplier linear = new MatrixMultiplierLinear();
        MatrixMultiplierParallel parallel = new MatrixMultiplierParallel();

        long startLinear = System.currentTimeMillis();
        result = invokeLinearMultiply(result, linear, left, right);
        long endLinear = System.currentTimeMillis();
        float avrglinearTime = (endLinear - startLinear) / ATTEMPTS;

        float[] a = new float[2 * k];
        result = invokeParallelMultiply(parallel, result, left, right, a, avrglinearTime);

        showTable(a, avrglinearTime);

        Matrix expectedResult = new Matrix();
        expectedResult.readFromFile(new File("TestData\\Ex2\\result"));

        if (!expectedResult.equals(result)) {
            throw new RuntimeException();
        }
    }

    private static void showTable(float[] a, float avrglinearTime) {
        System.out.printf("\n\n%-10s %-10s\n", "Cores Number", "Acceleration");
        for (int i = 1; i <= 2 * k; i++) {
            System.out.printf("#%-10d%-10.4f\n", i, a[i - 1]);
        }
        System.out.println("Average linear time: " + avrglinearTime + "ms");
    }

    private static Matrix invokeParallelMultiply(MatrixMultiplierParallel parallel, Matrix result, Matrix left, Matrix right, float[] a, float avrglinearTime) {
        int done = ATTEMPTS;
        try {
            for (int i = 1; i <= 2 * k; i++) {
                long startParallel = System.currentTimeMillis();
                for (int j = 0; j < ATTEMPTS; j++) {
                    parallel.setParallelismLevel(i);
                    result = parallel.multiply(left, right);
                    //showProgress(++done);
                }
                long endParallel = System.currentTimeMillis();
                float avrgParallelTime = (endParallel - startParallel) / ATTEMPTS;
                a[i - 1] = avrglinearTime / avrgParallelTime;
            }
        } catch (MatrixMultiplicationImpossible ex) {
            System.out.println(ERROR_MSG);
        }
        return result;
    }

    private static Matrix invokeLinearMultiply(Matrix result, MatrixMultiplier linear, Matrix left, Matrix right) {
        try {
            for (int i = 0; i < ATTEMPTS; i++) {
                result = linear.multiply(left, right);
                //showProgress(i);
            }
            
        } catch (MatrixMultiplicationImpossible ex) {
            System.out.println(ERROR_MSG);
            System.exit(1);
        }
        return result;
    }
}
