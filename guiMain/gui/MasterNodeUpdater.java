package gui;

public class MasterNodeUpdater implements Runnable{
	
	private Thread runner;
	
	public MasterNodeUpdater(){
		runner = new Thread(this);
		runner.start();
	}
	

	@Override
	public void run() {
		//repaint();
	}
	

}
