/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bg.uni_sofia.fmi.java.matrix_multiplication.parallel;

import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author a
 */
public class MatrixMultiplierTask1 extends RecursiveAction {

    private int THRESHOLD = 250;
    private Matrix left;
    private Matrix right;
    private Matrix result;
    private int begin;
    private int end;

    public MatrixMultiplierTask1(Matrix left, Matrix right, int begin, int end, Matrix result) {
        this.left = left;
        this.right = right;
        this.begin = begin;
        this.end = end;
        this.result = result;

    }

    @Override
    protected void compute() {
        if ((end - begin) <= THRESHOLD) {
            result.multiplyFromTo(left, right, begin, end);
            return;
        }
        int mid = (end + begin) / 2;
        MatrixMultiplierTask1 upTask = new MatrixMultiplierTask1(left, right, begin, mid, result);
        MatrixMultiplierTask1 downTask = new MatrixMultiplierTask1(left, right, mid, end, result);
        invokeAll(upTask, downTask);

    }

}
