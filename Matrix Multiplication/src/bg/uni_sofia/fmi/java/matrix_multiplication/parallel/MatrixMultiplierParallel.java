/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bg.uni_sofia.fmi.java.matrix_multiplication.parallel;

import bg.uni_sofia.fmi.java.matrix_multiplication.MatrixMultiplier;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.multi.MultiPanelUI;

/**
 *
 * @author a
 */

public class MatrixMultiplierParallel implements MatrixMultiplier {

	public class ComputeThread implements Runnable {

		private int data;
		private int k;
		private Matrix left, right, result;

		public ComputeThread(int l, Matrix left, Matrix right, Matrix result) {
			this.result = result;
			data = l;
			k = data;
			this.left = left;
			this.right = right;
		}

		@Override
		public void run() {
			double accumulate;

			for (int i = 0; i < left.getColumns(); i++) {

				for (int j = 0; j < right.getColumns(); j++) {
					accumulate = result.getElementAt(k, j)
							+ left.getElementAt(k, i)
							* right.getElementAt(i, j);

					result.setElementAt(k, j, accumulate);
				}
			}


		}
	}

	int parallelismLevel = 1;

	public void setParallelismLevel(int parallelismLevel) {
		this.parallelismLevel = parallelismLevel;
	}

	@Override
	public Matrix multiply(final Matrix left, final Matrix right)
			throws MatrixMultiplicationImpossible {
		return multiplyWithExecutor(left, right);
	}

	public Matrix multiplyWithTask(final Matrix left, final Matrix right)
			throws MatrixMultiplicationImpossible {

		ForkJoinPool pool = new ForkJoinPool(parallelismLevel);

		if (left.getColumns() != right.getRows()) {
			throw new MatrixMultiplicationImpossible();
		}

		Matrix result = new Matrix(left.getRows(), right.getColumns());

		try {
			// pool.invoke(new MatrixMultiplierTask2(left, right, result, -1,
			// 0));
			pool.invoke(new MatrixMultiplierTask1(left, right, 0, left
					.getRows(), result));

			return result;
		} finally {
			pool.shutdown();
		}
	}

	public Matrix multiplyWithExecutor(final Matrix left, final Matrix right)
			throws MatrixMultiplicationImpossible {

		ExecutorService executor = Executors
				.newFixedThreadPool(parallelismLevel);

		if (left.getColumns() != right.getRows()) {
			throw new MatrixMultiplicationImpossible();
		}

		Matrix result = new Matrix(left.getRows(), right.getColumns());

		for (int k = 0; k < left.getRows(); k++) {
			executor.execute(new ComputeThread(k, left, right, result));

		}

		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public Matrix multiplyWithThreads(final Matrix left, final Matrix right)
			throws MatrixMultiplicationImpossible {

		if (left.getColumns() != right.getRows()) {
			throw new MatrixMultiplicationImpossible();
		}

		Matrix result = new Matrix(left.getRows(), right.getColumns());
		
		 Thread threads[] = new Thread[parallelismLevel];
		for (int k = 0; k < parallelismLevel; k++) {
			threads[k] = new Thread( new ComputeThread(k, left, right, result));
			threads[k].start();

		}
		
		 for (int k = 0; k < parallelismLevel; k++)
		 try {
		 threads[k].wait(Long.MAX_VALUE);
		 } catch (InterruptedException e) {
		 e.printStackTrace();
		 }
		 
		 return result;
	}
	
}
