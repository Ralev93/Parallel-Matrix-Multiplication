package bg.uni_sofia.fmi.java.matrix_multiplication;

import java.io.File;

import bg.uni_sofia.fmi.java.matrix_multiplication.exceptions.InvalidOptionException;

public class Options {
	// A*B=C A(m, n) B(n, k)
	// -m
	private int m;
	// -n
	private int n;
	// -k
	private int k;
	// -i
	private File input;
	// -o
	private File output;
	// -q
	private boolean quiet;
	// -t
	private int threads;
	// -nogui
	private boolean showGUI;
	
	private boolean useFile;
	
	public Options(String[] options) throws InvalidOptionException {
		
		m=0;
		n=0;
		k=0;
		useFile = true;
		threads = Runtime.getRuntime().availableProcessors();
		quiet = false;
		showGUI = true;
		
		
		parseOptions(options);
	}

	public void parseOptions(String[] options) throws InvalidOptionException
	{
		for(int i=0; i<options.length; i++)
		{
			switch(options[i])
			{
				case "-m": m = Integer.parseInt(options[++i]);
							useFile = false;
							break;
				case "-n": n = Integer.parseInt(options[++i]);
							useFile = false;
							break;
				case "-k": k = Integer.parseInt(options[++i]);
							useFile = false;
							break;
				case "-i": input = new File(options[++i]);
							
							useFile = true;
							break;
				case "-o": output = new File(options[++i]);
							
							break;
				case "-nogui":
							showGUI = false;
							
							break;
						
				case "-q": quiet = true;
							
							break;
				case "-t":
				case "-tasks":
							threads = Integer.parseInt(options[++i]);
							
							break;
			}
		}
			
		if(showGUI)
			useFile = true;
		
		if (!showGUI && useFile && input == null)
			throw new InvalidOptionException("Incorrect input file path.");
			
			if(!showGUI && !useFile && (m <= 0 || n <= 0 || k <= 0))
				throw new InvalidOptionException("Incorrect dimensions of the matrices to multiply.");
			
		
	}
	
	public boolean shouldBeQuiet()
	{
		return quiet;
	}
	
	public boolean shouldShowGUI()
	{
		return showGUI;
	}
	
	public void setLeftRows(int m)
	{
		this.m = m;
	}
	
	public int getLeftRows()
	{
		return m;
	}
	
	public void setLeftColumns(int n)
	{
		this.n = n;
	}
	
	public int getLeftColumns()
	{
		return n;
	}
	
	public void setRightRows(int n)
	{
		this.n = n;
	}
	
	public int getRightRows()
	{
		return n;
	}
	
	public void setRightColumns(int k)
	{
		this.k = k;
	}
	
	public int getRightColumns()
	{
		return k;
	}
	
	public int getThreadsCount()
	{
		return threads;
	}
	
	public void setThreadsCount(int threadsCount)
	{
		threads = threadsCount;
	}
	
	public File getInputFile()
	{
		return input;
	}
	
	public File getOutputFile()
	{
		return output;
	}
	
	public boolean shouldUseFile()
	{
		return useFile;
	}
	
	public void setShouldUseFile( boolean useFile )
	{
		this.useFile = useFile;	
	}
}
