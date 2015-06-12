package bg.uni_sofia.fmi.java.matrix_multiplication;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import bg.uni_sofia.fmi.java.matrix_multiplication.GUI.MainWindow;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.IncorrectMatrixMultiplication;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.MatrixMultiplicationImpossible;
import bg.uni_sofia.fmi.java.matrix_multiplication.linear.MatrixMultiplierLinear;
import bg.uni_sofia.fmi.java.matrix_multiplication.log.Logger;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;
import bg.uni_sofia.fmi.java.matrix_multiplication.parallel.MatrixMultiplierParallel;

public class CalculationTask extends SwingWorker<Void, Void> {
	private int attempts;
	private int threads;
	private int totalIterations;
	private float avrglinearTime = 0;
	private float[] coresTimes;
	private int progress = 0;

	private File leftFile; // file, containing the left matrix;
	private File rightFile; // file, containing the right matrix;
	private File expectedResultFile; // file, containing the expected result
	private MainWindow mainWindow; // the graphical interface
	private Logger logger = new Logger(false);

	private Options options;
	private Matrix result;

	/* message, shown when the calculation is incorrect */
	private final static String MULTIPLICATION_INC_ERROR_MSG = "Wrong matrix multiplication!";
	/* Message, shown when multiplication is impossible */
	private final static String MULTIPLICATION_IMP_ERROR_MSG = "The columns of the first matrix must"
			+ " be equal of the rows of the second!\nTerminating.";

	private final static String FILE_ERROR_MSG = "No file selected!\nTerminating.";

	public CalculationTask(int attempts, Options options) {
		this.threads = options.getThreadsCount();
		this.options = options;
		this.attempts = attempts;
		this.coresTimes = new float[2 * threads];
		this.totalIterations = this.attempts + (2 * this.threads)
				* this.attempts;
		this.leftFile = options.getLeftInputFile();
		this.rightFile = options.getRightInputFile();
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

	public void setWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void setQuiet(boolean quiet) {
		this.logger.setQuiet(quiet);
	}

	public int getTotalIterations() {
		return totalIterations;
	}

	public int getAttempts() {
		return attempts;
	}

	private void showTable() {
		logger.log(String.format("\n\n%-10s %-10s %-10s\n", "Threads",
				"Acceleration", "ms"));
		for (int i = 1; i <= 2 * threads; i++) {
			logger.log(String.format("#%-10d%-10.4f %.1fms\n", i,
					coresTimes[i - 1], avrglinearTime / coresTimes[i - 1]));
		}
		logger.logln("Average linear time: " + avrglinearTime + "ms");
	}

	private Matrix invokeLinearMultiply(Matrix left, Matrix right,
			MatrixMultiplierParallel linear) {
		Matrix result = new Matrix();
		logger.logln("Linear multiplying started");
		linear.setParallelismLevel(1);
		try {
			long linearTime = 0;
			for (int i = 0; i < attempts && !this.isCancelled(); i++) {

				long startLinear = System.currentTimeMillis();
				result = linear.multiply(left, right);
				long endLinear = System.currentTimeMillis();

				linearTime += endLinear - startLinear;

				progress = i + 1;
				setProgress(progress);
			}
			avrglinearTime = linearTime / (float) attempts;
			logger.logln("Linear multiplying finished for: " + avrglinearTime);
		} catch (MatrixMultiplicationImpossible ex) {
			logger.log(MULTIPLICATION_IMP_ERROR_MSG, options.shouldShowGUI());
			System.exit(1);
		}
		return result;
	}

	private Matrix invokeParallelMultiply(Matrix left, Matrix right,
			MatrixMultiplierParallel parallel) {

		Matrix result = new Matrix();

		logger.logln("Parallel multiplying started");
		try {
			for (int i = 1; i <= 2 * threads && !this.isCancelled(); i++) {
				long parallelTime = 0;
				for (int j = 0; j < attempts && !this.isCancelled(); j++) {
					parallel.setParallelismLevel(i); // # threads

					long startParallel = System.currentTimeMillis();
					result = parallel.multiply(left, right);
					long endParallel = System.currentTimeMillis();
					parallelTime += endParallel - startParallel;

					setProgress(++progress);
				}
				float avrgParallelTime = parallelTime / attempts;
				coresTimes[i - 1] = avrglinearTime / avrgParallelTime;
			}
			logger.logln("Parallel multiplying finished.");

		} catch (MatrixMultiplicationImpossible ex) {
			logger.log(MULTIPLICATION_IMP_ERROR_MSG, options.shouldShowGUI());
			System.exit(1);
		}
		return result;
	}

	@Override
	protected Void doInBackground() throws Exception {

		if (options.shouldUseFile()
				&& (leftFile == null || rightFile == null || expectedResultFile == null)) {
			logger.log(FILE_ERROR_MSG, options.shouldShowGUI());
			/* close the window */// TODO: Nullpointer exception if no gui!!!
			if (options.shouldShowGUI())
				mainWindow.getJFrame().dispatchEvent(
						new WindowEvent(mainWindow.getJFrame(),
								WindowEvent.WINDOW_CLOSING));
			throw new FileNotFoundException();
		}

		Matrix left;
		Matrix right;

		if (!options.shouldUseFile() && !options.shouldShowGUI()) {
			left = new Matrix(options.getLeftRows(), options.getLeftColumns());
			right = new Matrix(options.getRightRows(), options.getRightColumns());

			left.generateRandom();
			right.generateRandom();
		} else {
			left = new Matrix(leftFile);
			right = new Matrix(rightFile);
		}

		setProgress(0);
		invokeLinearMultiply(left, right, new MatrixMultiplierParallel());
		result = invokeParallelMultiply(left, right, new MatrixMultiplierParallel());

		if (this.isCancelled()) {
			logger.logln("Calcuation aborted!");
			setProgress(0);
		}

		// if (!expectedResult.equals(result)) {
		// logger.log(MULTIPLICATION_INC_ERROR_MSG);
		// throw new IncorrectMatrixMultiplication();
		// }
		return null;
	}

	@Override
	public void done() {
		showTable();
		
		try {
			if (options.getOutputFile() != null) {
				result.writeToFile(options.getOutputFile());
			} else {
				JFileChooser chooser = mainWindow.getJFileChooser();
				int rVal = chooser.showSaveDialog(null); // or mainWindow.frame
				if (rVal == JFileChooser.APPROVE_OPTION) {
					System.out.println("Ei tuka: "
							+ chooser.getSelectedFile().getAbsolutePath());
					result.writeToFile(chooser.getSelectedFile());
				}
			}
		} catch (IOException e) {
			System.out.println("PROBLEM");
		}

		// btnCalculate.setEnabled(true);
		// setCursor(null); //turn off the wait cursor
	}

	public void call() throws Exception {
		doInBackground();
		done();
	}
}
