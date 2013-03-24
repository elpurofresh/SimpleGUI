package backend;

import gui.Gui;

public class ThreadManager implements Runnable{

	Gui window = null;
	
	public boolean writeDataToFile = false;
	public boolean runCondition = false;
	
	//Thread threadRunner = null;
	//ExperimentOne expOne = null;
	
	public ThreadManager(Gui window){
		this.window = window;
		//expOne = new ExperimentOne(window);
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (runCondition) {
			if (writeDataToFile) {
				writeDataToFile = false;
				window.fileLogger.WriteData(window.serialPortManager.netText);
			}
			
			/*if (window.isExpOneSelected()) {
				new Thread(expOne, "Exper. One").start();
				//threadRunner.start();
				//runExpOne();				
			} else if (!window.isExpOneSelected()) {
				
			}*/
			
		}
		
	}
	
	public void runExpOne(){
		int numTests = 10;
		int intervalTime = Integer.valueOf(window.textInterval.getText())*1000; //to convert to milliseconds
		long timeBefore = System.currentTimeMillis();
		
		for (int i = 0; i < numTests; i++) {
			window.serialPortManager.sendData(window.textOutputTest.getText());
			while ((System.currentTimeMillis() - timeBefore) < intervalTime) {
				//do nothing, just wait
			}
			timeBefore = System.currentTimeMillis();
			window.lblNumberOfTests.setText(Integer.toString(i+1));
		}
		window.textMsgArea.append("Finished Experiment 1!! \n");
		window.setExpOneSelected(false);
	}

	public boolean isWriteDataToFile() {
		return writeDataToFile;
	}

	public void setWriteDataToFile(boolean writeDataToFile) {
		this.writeDataToFile = writeDataToFile;
	}

}
