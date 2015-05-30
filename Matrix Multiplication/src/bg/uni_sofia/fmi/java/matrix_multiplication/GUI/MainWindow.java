package bg.uni_sofia.fmi.java.matrix_multiplication.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;

import bg.uni_sofia.fmi.java.matrix_multiplication.CalculationTask;
import javax.swing.JProgressBar;

public class MainWindow {

	private JFrame frame;
	private final File DEFAULT_DIR = new File( ".\\TestData\\ex1");
	private CalculationTask calcTask = new CalculationTask();
	
	private File getMatrixFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(DEFAULT_DIR);				
		
		int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile();
	    }
	    else return null; // TODO: throw exception!
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 406, 362);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JButton btnLoadFrstMatrix = new JButton("Load matrix 1");
		btnLoadFrstMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calcTask.setLeftFile(getMatrixFile());
			}
		});
		btnLoadFrstMatrix.setBounds(10, 28, 158, 23);
		frame.getContentPane().add(btnLoadFrstMatrix);
		
		
		JButton btnLoadSndMatrix = new JButton("Load matrix 2");
		btnLoadSndMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calcTask.setRightFile(getMatrixFile());
			}
		});
		btnLoadSndMatrix.setBounds(206, 28, 158, 23);
		frame.getContentPane().add(btnLoadSndMatrix);
		
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calcTask.execute();
			}
		});
		btnCalculate.setBounds(99, 119, 173, 90);
		frame.getContentPane().add(btnCalculate);
		
		
		JButton btnLoadExpectedResult = new JButton("TMP");
		btnLoadExpectedResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calcTask.setExpectedResultFile(getMatrixFile());
			}
		});
		btnLoadExpectedResult.setBounds(138, 72, 89, 23);
		frame.getContentPane().add(btnLoadExpectedResult);
		
		
		JProgressBar progressBar = new JProgressBar(0, CalculationTask.totalIterations);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 247, 354, 44);
		frame.getContentPane().add(progressBar);
	}
}
