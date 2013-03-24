package main;


public class ExperimentTwo implements Runnable{
	
Main main = null;
	
	public ExperimentTwo(Main main){
		this.main = main;
	}

	@Override
	public void run() {
		int numTests = 10;
		int intervalTime = Integer.valueOf(main.gui.textInterval.getText())*1000; //to convert to milliseconds
		long timeBefore = System.currentTimeMillis();
		
		for (int i = 0; i < numTests; i++) {
			main.serialPortManager.sendData("<Send>");//[Send]
			while ((System.currentTimeMillis() - timeBefore) < intervalTime) {
				//do nothing, just wait
			}
			timeBefore = System.currentTimeMillis();
			main.gui.lblNumberOfTests.setText("Number of Tests: " + Integer.toString(i+1));
		}
		main.gui.textMsgArea.append("Finished Experiment 2!! \n");
		main.gui.setExpTwoSelected(false);
		
	}

}
