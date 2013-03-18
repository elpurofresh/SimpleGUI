package guiMain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class FileReadWrite {
	GuiMain window = null;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
	Date date = new Date();

	String outputFileName = "C:\\andres\\Code\\eclipseWorkspace\\SimpleGUI\\guiMain\\data\\berExperimentLog" + dateFormat.format(date) + ".txt";
	File outputFile = new File(outputFileName);
	Writer output = null; 

	BufferedReader input = null;
	
	boolean firstWrite = true;

	public FileReadWrite(GuiMain window){
		this.window = window;
		initialization();
	}

	public void initialization()
	{
		try {
			output = new BufferedWriter(new FileWriter(outputFile, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Logging data to: " + outputFileName);
		/*
		// This is done here so the header files are done once 
		//without checking if there was a previous version of this file.
		try {
			output = new BufferedWriter(new FileWriter(outputFile));
			output.write("Network Data\t" +dateFormat.format(date) + "\n");
			output.append("botID\tParent\tTime Slot\t\n");			

			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	*/
	}
	
	public void WriteHeader(){
		System.out.println("Logging data to: " + outputFileName);

		// This is done here so the header files are done once 
		//without checking if there was a previous version of this file.
		try {
			output = new BufferedWriter(new FileWriter(outputFile, true));
			output.write("BER Experimental Data\t" +dateFormat.format(date) + "\n");
			output.append("Received data\t\n");			

			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Writes the contents of packageData to the desired outputFile
	public void WriteData(Package packageData){

		try {
			
			if (firstWrite == true) {
				output = new BufferedWriter(new FileWriter(outputFile, true));
				output.write("BER Experimental Data\t" + dateFormat.format(date) + "\n");
				output.append("Received data\t\n");
				output.close();
				firstWrite = false;
			}
			
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Writes the contents of packageData to the desired outputFile
	public void WriteData(byte[] data){

		String str;
		try {
			//output = new BufferedWriter(new FileWriter(outputFile));
			output = new BufferedWriter(new FileWriter(outputFile, true));
			for (int i = 0; i < window.serialPortManager.numBytesSent; i++) {
				str = new String(new byte[] { (byte) data[i] });
				output.append(str);
			}
			output.append('\n');
			//output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Example of reading data line by line from a given input file 
	public void ReadData(String fileToRead){

		String line;

		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileToRead)));

			while ((line = input.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(line, "\t");
				String value1 = token.nextToken();
				System.out.println(value1);
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
