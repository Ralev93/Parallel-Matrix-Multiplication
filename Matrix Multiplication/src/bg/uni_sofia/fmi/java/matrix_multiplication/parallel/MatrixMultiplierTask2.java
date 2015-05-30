/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bg.uni_sofia.fmi.java.matrix_multiplication.parallel;

import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import java.util.ArrayList;
import java.util.List;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author a
 */
public class MatrixMultiplierTask2 extends RecursiveAction {

    private int THRESHOLD = 250;
    private Matrix left;
    private Matrix right;
    private Matrix result;
    private int row;
    private int col;

    private List<MatrixMultiplierTask2> list = new ArrayList<>();

    public MatrixMultiplierTask2(Matrix left, Matrix right, Matrix result, int row, int col) {
        this.left = left;
        this.right = right;
        this.result = result;
        this.row = row;
        this.col = col;
    }

    @Override
    protected void compute() {

        if (row != -1) {
            double sum = 0;
            for (int i = 0; i < left.getColumns(); i++) {
                sum += left.getElementAt(row, i) * right.getElementAt(i, col);
            }
            result.setElementAt(row, col, sum);
            return;
        }
        
        for (int i = 0; i < left.getRows(); i++) {
            for (int j = 0; j < right.getColumns(); j++) {
                list.add(new MatrixMultiplierTask2(left, right, result, i, j));
            }
        }
        invokeAll(list);

        for (MatrixMultiplierTask2 task : list) {
            task.join();
        }
    }

}
