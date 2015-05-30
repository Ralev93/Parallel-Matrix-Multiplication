package bg.uni_sofia.fmi.java.matrix_multiplication;

import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;
import bg.uni_sofia.fmi.java.matrix_multiplication.linear.MatrixMultiplierLinear;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import bg.uni_sofia.fmi.java.matrix_multiplication.parallel.MatrixMultiplierParallel;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingWorker;

public class CalculationTask extends SwingWorker<Void, Void> {
	private final static int ATTEMPTS = 1;
	private static final int k = Runtime.getRuntime().availableProcessors();
	public final static int totalIterations = ATTEMPTS + (2 * k) * ATTEMPTS; // TODO:
																				// refactor
																				// public?
	private final static String ERROR_MSG = "The columns of the first matrix must"
			+ " be equal of the rows of the second!\nTerminating.";

	private File leftFile; // file, containing the left matrix;
	private File rightFile; // file, containing the right matrix;
	private File expectedResultFile; // file, containing the expected result
										// matrix;

	private int calcProgress(int done) {
//		System.out.printf("%.0f%% ", (float) done / totalIterations * 100);
		return (int) ((float) done / totalIterations * 100);// / totalIterations;
	}

	public void tmp() { //TODO: fix this gross name
		try {
			Matrix left = new Matrix();

			left.readFromFile(leftFile);

			Matrix right = new Matrix();
			right.readFromFile(rightFile);

			Matrix result = null;

			MatrixMultiplier linear = new MatrixMultiplierLinear();
			MatrixMultiplierParallel parallel = new MatrixMultiplierParallel();

			long startLinear = System.currentTimeMillis();
			result = invokeLinearMultiply(result, linear, left, right);
			long endLinear = System.currentTimeMillis();
			float avrglinearTime = (endLinear - startLinear) / ATTEMPTS;

			float[] a = new float[2 * k];
			result = invokeParallelMultiply(parallel, result, left, right, a,
					avrglinearTime);

			showTable(a, avrglinearTime);

			Matrix expectedResult = new Matrix();
			expectedResult.readFromFile(expectedResultFile);

			if (!expectedResult.equals(result)) {
				throw new RuntimeException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void showTable(float[] a, float avrglinearTime) {
		System.out.printf("\n\n%-10s %-10s\n", "Cores Number", "Acceleration");
		for (int i = 1; i <= 2 * k; i++) {
			System.out.printf("#%-10d%-10.4f\n", i, a[i - 1]);
		}
		System.out.println("Average linear time: " + avrglinearTime + "ms");
	}

	private Matrix invokeParallelMultiply(
			MatrixMultiplierParallel parallel, Matrix result, Matrix left,
			Matrix right, float[] a, float avrglinearTime) {
		int done = ATTEMPTS;
		try {
			for (int i = 1; i <= 2 * k; i++) {
				long startParallel = System.currentTimeMillis();
				for (int j = 0; j < ATTEMPTS; j++) {
					parallel.setParallelismLevel(i);
					result = parallel.multiply(left, right);
					setProgress(calcProgress(++done));
					System.out.println(calcProgress(done));
				}
				long endParallel = System.currentTimeMillis();
				float avrgParallelTime = (endParallel - startParallel)
						/ ATTEMPTS;
				a[i - 1] = avrglinearTime / avrgParallelTime;
			}
		} catch (MatrixMultiplicationImpossible ex) {
			System.out.println(ERROR_MSG);
		}
		return result;
	}

	private Matrix invokeLinearMultiply(Matrix result,
			MatrixMultiplier linear, Matrix left, Matrix right) {
		try {
			for (int i = 0; i < ATTEMPTS; i++) {
				result = linear.multiply(left, right);
				setProgress(calcProgress(i));
			}

		} catch (MatrixMultiplicationImpossible ex) {
			System.out.println(ERROR_MSG);
			System.exit(1);
		}
		return result;
	}

	public void setRightFile(File rightFile) {
		this.rightFile = rightFile;
	}

	public void setLeftFile(File leftFile) {
		this.leftFile = leftFile;
	}

	public void setExpectedResultFile(File expectedResultFile) {
		this.expectedResultFile = expectedResultFile;
	}

	@Override
	protected Void doInBackground() throws Exception {
         setProgress(0);
         tmp();
         return null;
	}
	
	@Override
    public void done() {
       // Toolkit.getDefaultToolkit().beep();
//        startButton.setEnabled(true);
//        setCursor(null); //turn off the wait cursor
//        taskOutput.append("Done!\n");
    }
}
