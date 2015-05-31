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
    public Matrix multiply(final Matrix left, final Matrix right)
            throws MatrixMultiplicationImpossible {
    	
        	
        	
        //ForkJoinPool pool = new ForkJoinPool(parallelismLevel);
        

        if (left.getColumns() != right.getRows()) {
            throw new MatrixMultiplicationImpossible();
        }
        
        Matrix result = new Matrix(left.getRows(), right.getColumns());
        
        
        class ComputeThread extends Thread {

        	private int data;
        	private int k;
        	Matrix result;
        	
        	   public ComputeThread(int l, Matrix result) {
        		this.result = result;
           		data = l;
           		k=data;
        	   }

        	   public void run() {
        		   double accumulate;
        		   
        		   for (int i = 0; i < left.getColumns(); i++) {
                   	
                       for (int j = 0; j < right.getColumns(); j++) {
                       	accumulate = result.getElementAt(k, j)+ left.getElementAt(k, i) * right.getElementAt(i, j);
                       	
                           result.setElementAt(k, j, accumulate); 
                       }
                   }
        		   
        		   accumulate = 10;
        	   }
        	}
        
        ComputeThread threads[] = new ComputeThread[left.getRows()];
        for (int k = 0; k < left.getRows(); k++) {
        	
        	 threads[k] = new ComputeThread(k, result);
        	threads[k].start();
            
        }
        
        for (int k = 0; k < left.getRows(); k++)
			try {
				threads[k].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        //System.out.println(result.toString());
//
//		try {
//			// pool.invoke(new MatrixMultiplierTask2(left, right, result, -1,
//			// 0));
//			pool.invoke(new MatrixMultiplierTask1(left, right, 0, left
//					.getRows(), result));
//
//			return result;
//		} finally {
//			pool.shutdown();
//		}
        return result;
    }

}


