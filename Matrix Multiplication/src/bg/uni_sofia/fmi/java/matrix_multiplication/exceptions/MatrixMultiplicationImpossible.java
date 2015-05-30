/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bg.uni_sofia.fmi.java.matrix_multiplication.exceptions;

public class MatrixMultiplicationImpossible extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MatrixMultiplicationImpossible() {
    }

    public MatrixMultiplicationImpossible(String message) {
        super(message);
    }

}
