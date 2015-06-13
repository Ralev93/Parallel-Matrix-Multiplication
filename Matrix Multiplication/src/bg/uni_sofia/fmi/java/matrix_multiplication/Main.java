package bg.uni_sofia.fmi.java.matrix_multiplication;


import bg.uni_sofia.fmi.java.matrix_multiplication.GUI.MainWindow;
import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.InvalidOptionException;

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

//			calcTask.setQuiet(options.shouldBeQuiet());

			try {
				calcTask.call();
			} catch (Exception e) {
				e.printStackTrace();
			}

        }
    }

}
