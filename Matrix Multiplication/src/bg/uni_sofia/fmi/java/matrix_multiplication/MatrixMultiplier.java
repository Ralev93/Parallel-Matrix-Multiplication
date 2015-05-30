package bg.uni_sofia.fmi.java.matrix_multiplication;

import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public interface MatrixMultiplier {

    Matrix multiply(Matrix left, Matrix right)
            throws MatrixMultiplicationImpossible;
}
