package guiMain;


public class ExperimentOne implements Runnable{
	
	GuiMain window = null;
	
	public ExperimentOne(GuiMain window){
		this.window = window;
	}

	@Override
	public void run() {
		int numTests = 10;
		int intervalTime = Integer.valueOf(window.textInterval.getText())*1000; //to convert to milliseconds
		long timeBefore = System.currentTimeMillis();

		for (int i = 0; i < numTests; i++) {
			window.serialPortManager.sendData(window.textOutputTest.getText());
			while ((System.currentTimeMillis() - timeBefore) < intervalTime) {
				//do nothing, just wait
			}
			timeBefore = System.currentTimeMillis();
			window.lblNumberOfTests.setText("Number of Tests: " + Integer.toString(i+1));
		}
		window.textMsgArea.append("Finished Experiment 1!! \n");
		window.setExpOneSelected(false);
		
	}

}
