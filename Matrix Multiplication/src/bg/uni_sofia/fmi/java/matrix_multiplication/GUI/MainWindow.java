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
	private JLabel lblAttempts;
	private JLabel lblMaxThreds;
	private JTextField txtThreads;

	private CalculationTask calcTask;
	private final File DEFAULT_DIR = new File(".\\TestData\\ex1");//TODO: UNIX?
	//private File leftFile = new File(".\\TestData\\ex1\\left.txt");
	//private File rightFile = new File(".\\TestData\\ex1\\right.txt");
	private File resFile = new File(".\\TestData\\ex1\\result.txt");
	private int attempts = 3;
	private boolean quiet = false;

	private static Options options;
	private JTextField txtLeftRows;
	private JTextField txtLeftCols;
	private JTextField txtRightCols;
	private JFileChooser chooser = new JFileChooser();

	public JFileChooser getJFileChooser() {
		return chooser;
	}
	
	public JFrame getJFrame() {
		return frame;
	}

	private File getMatrixFile() {
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
		frame.setBounds(100, 100, 320, 267);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		chooser.setCurrentDirectory(DEFAULT_DIR);

		/*
		 * Button for loading the first matrix
		 */
		JButton btnLoadFrstMatrix = new JButton("Load matrix 1");
		btnLoadFrstMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				options.setLeftInputFile(getMatrixFile());
			}
		});
		btnLoadFrstMatrix.setBounds(10, 28, 134, 23);
		frame.getContentPane().add(btnLoadFrstMatrix);

		/*
		 * Button for loading the second matrix
		 */
		JButton btnLoadSndMatrix = new JButton("Load matrix 2");
		btnLoadSndMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				options.setLeftInputFile(getMatrixFile());
			}
		});
		btnLoadSndMatrix.setBounds(154, 28, 142, 23);
		frame.getContentPane().add(btnLoadSndMatrix);

		/*
		 * Button for calculating the matrix
		 */

		btnCalculate = new JButton("Calculate");
		final MainWindow that = this; //TODO: REFACTOR
		btnCalculate.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {
				calcTask = new CalculationTask(attempts, options);
				calcTask.setWindow(that);
				calcTask.setExpectedResultFile(resFile);//TODO:delete
				options.setQuiet(quiet);
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
		btnCalculate.setBounds(154, 62, 89, 50);
		frame.getContentPane().add(btnCalculate);

		/*
		 * Progress bar
		 */
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 169, 286, 44);
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
		btnAbout.setBounds(277, 123, 20, 23);
		try {
			Image img = ImageIO.read(new File(".\\resources\\Help.png"))
					.getScaledInstance(btnAbout.getHeight(),
							btnAbout.getWidth(), java.awt.Image.SCALE_SMOOTH);
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
		txtAttempts.setText(Integer.toString(1));
		/*
		 * Button stop
		 */
		btnStop = new JButton("");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calcTask.cancel(true);
			}
		});
		btnStop.setBounds(253, 62, 43, 50);
		try {
			Image img = ImageIO.read(new File(".\\resources\\Stop.png"))
					.getScaledInstance(btnStop.getHeight(), btnStop.getWidth(),
							java.awt.Image.SCALE_SMOOTH);
			btnStop.setIcon(new ImageIcon(img));
		} catch (IOException e) {
		}
		frame.getContentPane().add(btnStop);

		/*
		 * Labels
		 */

		lblAttempts = new JLabel("Attempts: ");
		lblAttempts.setBounds(10, 61, 63, 23);
		frame.getContentPane().add(lblAttempts);

		lblMaxThreds = new JLabel("Max# Threds:");
		lblMaxThreds.setBounds(10, 80, 77, 23);
		frame.getContentPane().add(lblMaxThreds);

		/*
		 * Text field for the number of threads
		 */
		txtThreads = new JTextField();
		txtThreads.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				options.setThreadsCount(Integer.parseInt(txtThreads.getText()));
			}
		});
		txtThreads.setColumns(10);
		txtThreads.setBounds(94, 81, 50, 20);
		frame.getContentPane().add(txtThreads);
		txtThreads.setText(Integer.toString(options.getThreadsCount()));

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
		chckbxQuiet.setBounds(154, 119, 77, 23);
		frame.getContentPane().add(chckbxQuiet);
		if (options.shouldBeQuiet()) {
			quiet = true;
			chckbxQuiet.setSelected(true);
		}

		/*
		 * Label left columns
		 */
		JLabel lblLeftColumns = new JLabel("Left columns:");
		lblLeftColumns.setBounds(10, 122, 87, 14);
		frame.getContentPane().add(lblLeftColumns);

		/*
		 * Label left rows
		 */
		JLabel lblLeftRows = new JLabel("Left rows:");
		lblLeftRows.setBounds(10, 103, 87, 14);
		frame.getContentPane().add(lblLeftRows);

		/*
		 * Label right columns
		 */
		JLabel lblRightColumns = new JLabel("Right columns:");
		lblRightColumns.setBounds(10, 141, 77, 14);
		frame.getContentPane().add(lblRightColumns);

		/*
		 * Text field for the left rows
		 */
		txtLeftRows = new JTextField();
		txtLeftRows.setColumns(10);
		txtLeftRows.setBounds(94, 100, 50, 20);
		frame.getContentPane().add(txtLeftRows);
		txtLeftRows.setText(Integer.toString(options.getLeftRows()));

		/*
		 * Text field for the left columns
		 */
		txtLeftCols = new JTextField();
		txtLeftCols.setColumns(10);
		txtLeftCols.setBounds(94, 119, 50, 20);
		frame.getContentPane().add(txtLeftCols);
		txtLeftCols.setText(Integer.toString(options.getLeftColumns()));

		/*
		 * Text field for the right columns
		 */
		txtRightCols = new JTextField();
		txtRightCols.setColumns(10);
		txtRightCols.setBounds(94, 138, 50, 20);
		frame.getContentPane().add(txtRightCols);
		txtRightCols.setText(Integer.toString(options.getRightColumns()));

	}
}
