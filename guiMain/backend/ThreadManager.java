package backend;

import main.Main;

public class ThreadManager implements Runnable{

	Main main = null;
	
	public boolean writeDataToFile = false;
	public boolean runCondition = false;
	
	//Thread threadRunner = null;
	//ExperimentOne expOne = null;
	
	public ThreadManager(Main main){
		this.main = main;
		//expOne = new ExperimentOne(window);
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (runCondition) {
			if (writeDataToFile) {
				writeDataToFile = false;
				main.fileLogger.WriteData(main.serialPortManager.netText);
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
		int intervalTime = Integer.valueOf(main.gui.textInterval.getText())*1000; //to convert to milliseconds
		long timeBefore = System.currentTimeMillis();
		
		for (int i = 0; i < numTests; i++) {
			main.serialPortManager.sendData(main.gui.textOutputTest.getText());
			while ((System.currentTimeMillis() - timeBefore) < intervalTime) {
				//do nothing, just wait
			}
			timeBefore = System.currentTimeMillis();
			main.gui.lblNumberOfTests.setText(Integer.toString(i+1));
		}
		main.gui.textMsgArea.append("Finished Experiment 1!! \n");
		main.gui.setExpOneSelected(false);
	}

	public boolean isWriteDataToFile() {
		return writeDataToFile;
	}

	public void setWriteDataToFile(boolean writeDataToFile) {
		this.writeDataToFile = writeDataToFile;
	}

}
