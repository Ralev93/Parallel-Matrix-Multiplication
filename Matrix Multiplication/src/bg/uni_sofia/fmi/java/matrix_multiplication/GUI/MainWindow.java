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

import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class MainWindow {

	private JFrame frame;
	private JButton btnCalculate;
	private JProgressBar progressBar;
	private final File DEFAULT_DIR = new File(".\\TestData\\ex1");
	private CalculationTask calcTask;// = new CalculationTask();
	private JButton btnNewButton;
	private JTextField textField;

	private File leftFile = new File(".\\TestData\\ex1\\left");
	private File rightFile = new File(".\\TestData\\ex1\\right");
	private File resFile = new File(".\\TestData\\ex1\\result");
	private int attempts;

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
		frame.setBounds(100, 100, 406, 261);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnLoadFrstMatrix = new JButton("Load matrix 1");
		btnLoadFrstMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// calcTask.setLeftFile(getMatrixFile());
				leftFile = getMatrixFile();
			}
		});
		btnLoadFrstMatrix.setBounds(10, 28, 158, 23);
		frame.getContentPane().add(btnLoadFrstMatrix);

		JButton btnLoadSndMatrix = new JButton("Load matrix 2");
		btnLoadSndMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// calcTask.setRightFile(getMatrixFile());
				rightFile = getMatrixFile();
			}
		});
		btnLoadSndMatrix.setBounds(222, 28, 158, 23);
		frame.getContentPane().add(btnLoadSndMatrix);

		btnCalculate = new JButton("Calculate");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				calcTask = new CalculationTask();
				calcTask.setFrame(frame);
				calcTask.setLeftFile(leftFile);
				calcTask.setRightFile(rightFile);
				calcTask.setExpectedResultFile(resFile);
				calcTask.setAttempts(attempts);
				progressBar.setMaximum(calcTask.getTotalIterations());

				calcTask.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						if ("progress" == evt.getPropertyName()) {
							int progress = (Integer) evt.getNewValue();
							progressBar.setValue(progress);
						}
					}
				});

				calcTask.execute();
			}
		});
		btnCalculate.setBounds(98, 62, 173, 90);
		frame.getContentPane().add(btnCalculate);

		JButton btnLoadExpectedResult = new JButton("TMP");
		btnLoadExpectedResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// calcTask.setExpectedResultFile(getMatrixFile());
				resFile = getMatrixFile();
			}
		});
		btnLoadExpectedResult.setBounds(186, 28, 26, 23);
		frame.getContentPane().add(btnLoadExpectedResult);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 163, 370, 44);
		frame.getContentPane().add(progressBar);

		btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About ab = new About();
				ab.setVisible(true);
			}
		});
		btnNewButton.setBounds(314, 90, 50, 35);
		try {
			Image img = ImageIO.read(new File(".\\resources\\Help.png"))
					.getScaledInstance(50, 35, java.awt.Image.SCALE_SMOOTH);
			;
			btnNewButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		frame.getContentPane().add(btnNewButton);

		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				attempts = Integer.parseInt(textField.getText());
			}
		});
		textField.setBounds(10, 97, 50, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

	}
}
