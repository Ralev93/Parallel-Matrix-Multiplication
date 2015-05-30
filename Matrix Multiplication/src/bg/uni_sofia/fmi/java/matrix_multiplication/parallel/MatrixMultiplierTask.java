/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bg.uni_sofia.fmi.java.matrix_multiplication.parallel;

import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author a
 */
public class MatrixMultiplierTask extends RecursiveTask<Matrix> {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//    private static final long serialVersionUID = -49761584124622610L;
    private Matrix left;
    private Matrix right;
    private Matrix result;

    private int beginRow;
    private int endRow;
    private int beginCol;
    private int endCol;

    public MatrixMultiplierTask(Matrix left, Matrix right, Matrix result,
            int beginRow, int endRow, int beginCol, int endCol) {
        this.left = left;
        this.right = right;
        this.result = result;
        this.beginRow = beginRow;
        this.endRow = endRow;
        this.beginCol = beginCol;
        this.endCol = endCol;
    }

    @Override
    protected Matrix compute() {
        if ((endRow - beginRow) == 1 && (endCol - beginCol) == 1) {
//            double[][] subMatrix = new double[2][2];
//            subMatrix[beginRow][beginCol]
//                    = left.getElementAt(beginRow, 0) * right.getElementAt(0, beginCol)
//                    + left.getElementAt(beginRow, 1) * right.getElementAt(1, beginCol);
            double[][] subMatrix = new double[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    // subMatrix[i][j] = left.getElementAt(beginRow + i, beginCol + j)
                }
            }

            Matrix r = new Matrix(subMatrix);
            System.out.println("com: " + beginRow + " " + beginCol);
            System.out.println("IN COMPUTE \n" + r);
            return r;
        }

        int midRow = this.beginRow + (this.endRow - this.beginRow) / 2;
        int midCol = this.beginCol + (this.endCol - this.beginCol) / 2;

        MatrixMultiplierTask upLeft = new MatrixMultiplierTask(left, right, result, beginRow, midRow, beginCol, midCol);
        MatrixMultiplierTask upRight = new MatrixMultiplierTask(left, right, result, beginRow, midRow, midCol, endCol);
        MatrixMultiplierTask downLeft = new MatrixMultiplierTask(left, right, result, midRow, endRow, beginCol, midCol); // end  - endRow
        MatrixMultiplierTask downRight = new MatrixMultiplierTask(left, right, result, midRow, endRow, midCol, endCol);

        invokeAll(upLeft, upRight, downLeft, downRight);

//        return new Matrix(upLeft.join(), upRight.join(),
//                downLeft.join(), downRight.join());
//        downTask.join();
        return new Matrix();
    }

}
