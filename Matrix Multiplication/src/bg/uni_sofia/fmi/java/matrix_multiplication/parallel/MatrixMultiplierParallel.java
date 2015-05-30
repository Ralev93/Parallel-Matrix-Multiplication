/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bg.uni_sofia.fmi.java.matrix_multiplication.parallel;

import bg.uni_sofia.fmi.java.matrix_multiplication.MatrixMultiplier;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import java.util.concurrent.ForkJoinPool;

/**
 *
 * @author a
 */
public class MatrixMultiplierParallel implements MatrixMultiplier {

    int parallelismLevel = 1;

    public void setParallelismLevel(int parallelismLevel) {
        this.parallelismLevel = parallelismLevel;
    }

    @Override
    public Matrix multiply(Matrix left, Matrix right)
            throws MatrixMultiplicationImpossible {

        ForkJoinPool pool = new ForkJoinPool(parallelismLevel);
        Matrix result = new Matrix(left.getRows(), right.getColumns());

        if (left.getColumns() != right.getRows()) {
            throw new MatrixMultiplicationImpossible();
        } else {
            result = new Matrix(left.getRows(), right.getColumns());
        }
        try {
//            pool.invoke(new MatrixMultiplierTask2(left, right, result, -1, 0));
              pool.invoke(new MatrixMultiplierTask1(left, right, 0, left.getRows(), result));

            return result;
        } finally {
            pool.shutdown();
        }

    }

}
