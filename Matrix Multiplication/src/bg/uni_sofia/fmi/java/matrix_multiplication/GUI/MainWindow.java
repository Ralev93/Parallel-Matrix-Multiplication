package bg.uni_sofia.fmi.java.matrix_multiplication.GUI;

import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import bg.uni_sofia.fmi.java.matrix_multiplication.CalculationTask;
import bg.uni_sofia.fmi.java.matrix_multiplication.Options;

import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWindow {

	private JFrame frame;
	private JButton btnCalculate;
	private JProgressBar progressBar;
	private JButton btnAbout;
	private JTextField txtAttempts;
	private JButton btnStop;
	private JLabel lblNewLabel;
	private JLabel lblMaxThreds;
	private JTextField txtThreds;

	private CalculationTask calcTask;
	private final File DEFAULT_DIR = new File(".\\TestData\\ex1");
	private File leftFile = new File(".\\TestData\\ex1\\left.txt");
	private File rightFile = new File(".\\TestData\\ex1\\right.txt");
	private File resFile = new File(".\\TestData\\ex1\\result.txt");
	private int attempts = 3;
	private boolean quiet = false;
	
	private static Options options;
	

	private File getMatrixFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(DEFAULT_DIR);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else
			return null; // TODO: throw exception!
	}

	public JButton getBtnCalculate() {
		return btnCalculate;
	}

	/**
	 * Launch the application.
	 */
	public static void open(final Options options) {
		MainWindow.options = options;
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
		frame.setBounds(100, 100, 363, 261);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/*
		 * Button for loading the first matrix
		 */
		JButton btnLoadFrstMatrix = new JButton("Load matrix 1");
		btnLoadFrstMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				leftFile = getMatrixFile();
			}
		});
		btnLoadFrstMatrix.setBounds(10, 28, 158, 23);
		frame.getContentPane().add(btnLoadFrstMatrix);

		/*
		 * Button for loading the second matrix
		 */
		JButton btnLoadSndMatrix = new JButton("Load matrix 2");
		btnLoadSndMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rightFile = getMatrixFile();
			}
		});
		btnLoadSndMatrix.setBounds(178, 28, 158, 23);
		frame.getContentPane().add(btnLoadSndMatrix);
		
		/*
		 * Button for calculating the matrix
		 */

		btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calcTask = new CalculationTask(attempts, options);
				calcTask.setFrame(frame);
				calcTask.setLeftFile(leftFile);
				calcTask.setRightFile(rightFile);
				calcTask.setExpectedResultFile(resFile);
				calcTask.setQuiet(quiet);
				calcTask.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						if ("progress" == evt.getPropertyName()) {
							int progress = (Integer) evt.getNewValue();
							progressBar.setValue(progress);
						}
					}
				});
				progressBar.setMaximum(calcTask.getTotalIterations());
				calcTask.execute();
			}
		});
		btnCalculate.setBounds(178, 62, 105, 50);
		frame.getContentPane().add(btnCalculate);

		/*
		 * Progress bar
		 */
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 157, 326, 44);
		frame.getContentPane().add(progressBar);

		/*
		 * Button for the about section
		 */
		btnAbout = new JButton("");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About ab = new About();
				ab.setVisible(true);
			}
		});
		btnAbout.setBounds(314, 123, 20, 23);
		try {
			Image img = ImageIO.read(new File(".\\resources\\Help.png"))
					.getScaledInstance(btnAbout.getHeight(), btnAbout.getWidth(), java.awt.Image.SCALE_SMOOTH);
			btnAbout.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		frame.getContentPane().add(btnAbout);
		
		/*
		 * Text field for the number of attempts
		 */

		txtAttempts = new JTextField();
		txtAttempts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				attempts = Integer.parseInt(txtAttempts.getText());
			}
		});
		txtAttempts.setBounds(94, 62, 50, 20);
		frame.getContentPane().add(txtAttempts);
		txtAttempts.setColumns(10);

		/*
		 * Button stop
		 */
		btnStop = new JButton("");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calcTask.cancel(true);
			}
		});
		btnStop.setBounds(293, 62, 43, 50);
		try {
			Image img = ImageIO.read(new File(".\\resources\\Stop.png"))
					.getScaledInstance(btnStop.getHeight(), btnStop.getWidth(), java.awt.Image.SCALE_SMOOTH);
			btnStop.setIcon(new ImageIcon(img));
		} catch (IOException e) {
		}
		frame.getContentPane().add(btnStop);
		
		/*
		 * Labels
		 */
		
		lblNewLabel = new JLabel("Attempts: ");
		lblNewLabel.setBounds(10, 61, 63, 23);
		frame.getContentPane().add(lblNewLabel);
		
		lblMaxThreds = new JLabel("Max# Threds:");
		lblMaxThreds.setBounds(10, 83, 77, 23);
		frame.getContentPane().add(lblMaxThreds);
		
		/*
		 * Text field for the number of threads
		 */
		txtThreds = new JTextField();
		txtThreds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				options.setThreadsCount( Integer.parseInt(txtThreds.getText()));
			}
		});
		txtThreds.setColumns(10);
		txtThreds.setBounds(94, 81, 50, 20);
		frame.getContentPane().add(txtThreds);
		
		/*
		 * Check box for quiet mode
		 */
		final JCheckBox chckbxQuiet = new JCheckBox("Quiet\r\n");
		chckbxQuiet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				quiet = chckbxQuiet.isSelected();
			}
		});
		chckbxQuiet.setHorizontalAlignment(SwingConstants.LEFT);
		chckbxQuiet.setBounds(10, 105, 77, 23);
		frame.getContentPane().add(chckbxQuiet);

	}
}
