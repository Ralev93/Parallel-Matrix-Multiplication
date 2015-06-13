package bg.uni_sofia.fmi.java.matrix_multiplication.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Font;

import javax.swing.UIManager;

@SuppressWarnings("serial")
public class About extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					About frame = new About();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public About() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 385, 167);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea txtrThisApplicationComes = new JTextArea();
		txtrThisApplicationComes.setBackground(UIManager.getColor("Button.background"));
		txtrThisApplicationComes.setFont(new Font("Monospaced", Font.PLAIN, 15));
		txtrThisApplicationComes.setText("This application comes to you with \r\nthe collaboration of\r\nHristo Ralev & Nedko Savov.");
		txtrThisApplicationComes.setBounds(10, 24, 354, 80);
		contentPane.add(txtrThisApplicationComes);
	}
}
