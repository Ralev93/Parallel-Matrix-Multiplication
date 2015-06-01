package bg.uni_sofia.fmi.java.matrix_multiplication;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.IncorrectMatrixMultiplication;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;
import bg.uni_sofia.fmi.java.matrix_multiplication.linear.MatrixMultiplierLinear;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import bg.uni_sofia.fmi.java.matrix_multiplication.parallel.MatrixMultiplierParallel;

public class CalculationTask extends SwingWorker<Void, Void> {
	private int ATTEMPTS = 3;
	private static final int k = Runtime.getRuntime().availableProcessors();
	private int totalIterations = this.getAttempts() + (2 * k) * this.getAttempts();

	private float avrglinearTime = 0;
	private float[] coresTimes = new float[2 * k];
	private int progress = 0;

	private File leftFile; // file, containing the left matrix;
	private File rightFile; // file, containing the right matrix;
	private File expectedResultFile; // file, containing the expected result
	private JFrame mainFrame; // the graphical interface

	private final static String MULTIPLICATION_ERROR_MSG = "The columns of the first matrix must"
			+ " be equal of the rows of the second!\nTerminating.";

	private final static String FILE_ERROR_MSG = "No file selected!\nTerminating.";

	public void setRightFile(File rightFile) {
		this.rightFile = rightFile;
	}

	public void setLeftFile(File leftFile) {
		this.leftFile = leftFile;
	}

	public void setExpectedResultFile(File expectedResultFile) {
		this.expectedResultFile = expectedResultFile;
	}
	
	public void setFrame(JFrame frame) {
		mainFrame = frame;
		
	}

	public int getTotalIterations() {
		return totalIterations;
	}
	
	public int getAttempts() {
		return ATTEMPTS;
	}
//	private int calcProgress(int progress) {
////		return (int) ((float) progress / totalIterations * 100);
//		return progress;
//	}

	private void showTable() {
		System.out.printf("\n\n%-10s %-10s\n", "Cores", "Acceleration");
		for (int i = 1; i <= 2 * k; i++) {
			System.out.printf("#%-10d%-10.4f\n", i, coresTimes[i - 1]);
		}
		System.out.println("Average linear time: " + avrglinearTime + "ms");
	}

	private Matrix invokeLinearMultiply(Matrix left, Matrix right,
			MatrixMultiplier linear) {
		Matrix result = new Matrix();
		System.out.println("Linear multiplying started");
		try {
			long linearTime = 0;
			for (int i = 0; i < ATTEMPTS; i++) {

				long startLinear = System.currentTimeMillis();
				result = linear.multiply(left, right);
				long endLinear = System.currentTimeMillis();

				linearTime += endLinear - startLinear;

				progress = i + 1;
				setProgress(progress);
			}
			avrglinearTime = linearTime / ATTEMPTS;
		System.out.println("Linear multiplying finished for: " + avrglinearTime);
		} catch (MatrixMultiplicationImpossible ex) {
			JOptionPane.showMessageDialog(null, MULTIPLICATION_ERROR_MSG,
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return result;
	}

	private Matrix invokeParallelMultiply(Matrix left, Matrix right,
			MatrixMultiplierParallel parallel) {

		Matrix result = new Matrix();
		
		System.out.println("Parallel multiplying started");
		try {
			for (int i = 1; i <= 2 * k; i++) {
				long parallelTime = 0;
				for (int j = 0; j < ATTEMPTS; j++) {
					parallel.setParallelismLevel(i); // # threads

					long startParallel = System.currentTimeMillis();
					result = parallel.multiply(left, right);
					long endParallel = System.currentTimeMillis();
					parallelTime += endParallel - startParallel;

					setProgress(++progress);
				}
				float avrgParallelTime = parallelTime / ATTEMPTS;
				coresTimes[i - 1] = avrglinearTime / avrgParallelTime;
			}
			System.out.println("Parallel multiplying finished.");
		} catch (MatrixMultiplicationImpossible ex) {
			JOptionPane.showMessageDialog(null, MULTIPLICATION_ERROR_MSG,
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return result;
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (leftFile == null || rightFile == null || expectedResultFile == null) {
			JOptionPane.showMessageDialog(null, FILE_ERROR_MSG, "Error",
					JOptionPane.ERROR_MESSAGE);
			mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
			throw new FileNotFoundException();
		}

		Matrix left = new Matrix();
		left.readFromFile(leftFile);

		Matrix right = new Matrix();
		right.readFromFile(rightFile);

		Matrix expectedResult = new Matrix();
		expectedResult.readFromFile(expectedResultFile);

		Matrix result = null;

		MatrixMultiplier linear = new MatrixMultiplierLinear();
		MatrixMultiplierParallel parallel = new MatrixMultiplierParallel();

		setProgress(progress);

		result = invokeLinearMultiply(left, right, linear);
		result = invokeParallelMultiply(left, right, parallel);

		if (!expectedResult.equals(result)) {
			JOptionPane.showMessageDialog(null,
					"Incorrect matrix multiplication!", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw new IncorrectMatrixMultiplication();
		}
		return null;
	}

	@Override
	public void done() {
		Toolkit.getDefaultToolkit().beep();
		showTable();
		// btnCalculate.setEnabled(true);
		// setCursor(null); //turn off the wait cursor
	}

	public void setAttempts(int attempts2) {
		ATTEMPTS = attempts2;
		totalIterations = ATTEMPTS + (2 * k) * ATTEMPTS;

		
	}


}
