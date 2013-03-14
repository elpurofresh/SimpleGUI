package guiMain;

public class ProtocolControl implements Runnable{

	GuiMain window = null;
	public boolean runCondition	= true;
	public boolean flagTx		= false;
	public boolean flagACKTx 	= false;
	public boolean flagACKEnd	= false;
	public boolean flagACKFinal	= false;
	public boolean flagRxMode	= false;

	public final String msgTx		= "[";
	public final String msgACKTx	= "<";
	public final String msgEndData 	= "]";
	public final String msgACKEnd	= ">";

	public ProtocolControl(GuiMain window){
		this.window = window;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("GOT IN" + runCondition);

		while (runCondition) {

			//Receiver
			if (flagACKTx && flagRxMode) {
				window.serialPortManager.sendData(msgACKTx);
				System.out.println(msgACKTx);
				
			} else if (!flagACKTx && flagRxMode) {
				window.serialPortManager.sendData(msgACKEnd);
				System.out.println(msgACKEnd);
			}

			//Transmitter
			if (flagACKTx && !flagRxMode) {
				window.serialPortManager.sendData(window.textOutputTest.getText());
				window.serialPortManager.sendData(msgEndData);
				System.out.println(window.textOutputTest.getText());
			}
			System.out.println("GOT IN2" + runCondition);
		}
	}

	public void startComm(){
		window.serialPortManager.sendData(msgTx);
	}

	public boolean isFlagTx() {
		return flagTx;
	}

	public void setFlagTx(boolean flagTx) {
		this.flagTx = flagTx;
	}

	public boolean isFlagACKTx() {
		return flagACKTx;
	}

	public void setFlagACKTx(boolean flagACKTx) {
		this.flagACKTx = flagACKTx;
	}

	public boolean isFlagACKEnd() {
		return flagACKEnd;
	}

	public void setFlagACKEnd(boolean flagACKEnd) {
		this.flagACKEnd = flagACKEnd;
	}

	public boolean isRunCondition() {
		return runCondition;
	}

	public void setRunCondition(boolean runCondition) {
		this.runCondition = runCondition;
	}

	public boolean isFlagRxMode() {
		return flagRxMode;
	}

	public void setFlagRxMode(boolean flagRxMode) {
		this.flagRxMode = flagRxMode;
	}

	public boolean isFlagACKFinal() {
		return flagACKFinal;
	}

	public void setFlagACKFinal(boolean flagACKFinal) {
		this.flagACKFinal = flagACKFinal;
	}

}
