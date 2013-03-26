package main;


public class ExperimentOne implements Runnable{
	
	Main main = null;
	
	public ExperimentOne(Main main){
		this.main = main;
	}

	@Override
	public void run() {
		int numTests = 10;
		int intervalTime = Integer.valueOf(main.gui.textInterval.getText())*1000; //to convert to milliseconds
		long timeBefore = System.currentTimeMillis();

		for (int i = 0; i < numTests; i++) {
			main.serialPortManager.sendData(main.gui.textOutputTest.getText());
			main.gui.animation.updateMasterNodePic("Sending");
			
			while ((System.currentTimeMillis() - timeBefore) < intervalTime) {
				//do nothing, just wait
				main.gui.animation.updateMasterNodePic("Idle");
				main.gui.animation.repaint();
			}
			timeBefore = System.currentTimeMillis();
			main.gui.lblNumberOfTests.setText("Number of Tests: " + Integer.toString(i+1));
		}
		main.gui.textMsgArea.append("Finished Experiment 1!! \n");
		main.gui.animation.updateMasterNodePic("Idle");
		main.gui.setExpOneSelected(false);
		
	}

}
