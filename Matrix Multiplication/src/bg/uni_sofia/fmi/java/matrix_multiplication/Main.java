package bg.uni_sofia.fmi.java.matrix_multiplication;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import bg.uni_sofia.fmi.java.matrix_multiplication.GUI.MainWindow;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.InvalidOptionException;
import bg.uni_sofia.fmi.java.matrix_multiplication.matrix.Matrix;

public class Main {

	private final static int attempts = 3;
	
    public static void main(String[] args) {
    	
        Options options = null;
		try {
			options = new Options(args);
		} catch (InvalidOptionException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
        
        if(options.shouldShowGUI())
        {
        	MainWindow.open(options);
        }
        else
        {
        	CalculationTask calcTask = new CalculationTask(attempts, options);
//			calcTask.setFrame(frame);
//			calcTask.setLeftFile(leftFile);
//			calcTask.setRightFile(rightFile);
//			calcTask.setExpectedResultFile(resFile);
//			calcTask.setQuiet(options.shouldBeQuiet());

			try {
				calcTask.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			try {
//				calcTask.wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
    }

}
