package bg.uni_sofia.fmi.java.matrix_multiplication.log;

import javax.swing.JOptionPane;

public class Logger {

	private boolean quiet = false;

	public void log(String msg, boolean hasGUI) {

		if (!hasGUI) {
			if (!quiet) {
				System.out.print(msg);
			}
		} else {
			/* The only GUI messages in the app are error messages */
			JOptionPane.showMessageDialog(null, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
			// TODO: decide whether to show error msg in quiet mode or no!
		}

	}

	public void log(String msg) {
		this.log(msg, false);
	}

	public void logln(String msg) {
		this.log(msg + "\n", false);
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}
}
