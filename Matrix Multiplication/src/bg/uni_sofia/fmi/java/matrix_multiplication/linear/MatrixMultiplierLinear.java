/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bg.uni_sofia.fmi.java.matrix_multiplication.linear;

import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import bg.uni_sofia.fmi.java.matrix_multiplication.MatrixMultiplier;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;


public class MatrixMultiplierLinear implements MatrixMultiplier {

    @Override
    public Matrix multiply(Matrix left, Matrix right)
            throws MatrixMultiplicationImpossible {

        Matrix result;
        if (left.getColumns() != right.getRows()) {
            throw new MatrixMultiplicationImpossible();
        } else {
            result = new Matrix(left.getRows(), right.getColumns());
        }

        result.multiplyFromTo(left, right, 0, left.getRows());
        return result;
    }

}
