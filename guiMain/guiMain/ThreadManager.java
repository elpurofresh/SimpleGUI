package guiMain;

public class ThreadManager implements Runnable{

	GuiMain window = null;
	
	public boolean writeDataToFile = false;
	public boolean runCondition = false;
	
	public ThreadManager(GuiMain window){
		this.window = window;
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (runCondition) {
			
			if (writeDataToFile) {
				writeDataToFile = false;
				window.fileLogger.WriteData(window.serialPortManager.netText);
			}
		}
		
	}




	public boolean isWriteDataToFile() {
		return writeDataToFile;
	}

	public void setWriteDataToFile(boolean writeDataToFile) {
		this.writeDataToFile = writeDataToFile;
	}

}
