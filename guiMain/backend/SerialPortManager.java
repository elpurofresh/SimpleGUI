package backend;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import main.Main;

/**
 * @author andres
 *
 */
public class SerialPortManager implements SerialPortEventListener{

	//passed from main GUI
	Main main = null;

	//for containing the ports that will be found
	@SuppressWarnings("rawtypes")
	private Enumeration ports = null;
	//map the port names to CommPortIdentifiers
	private HashMap<String, CommPortIdentifier> portMap = new HashMap<String, CommPortIdentifier>();

	//this is the object that contains the opened port
	private CommPortIdentifier selectedPortIdentifier = null;
	private SerialPort serialPort = null;

	//input and output streams for sending and receiving data
	private InputStream input = null;
	private OutputStream output = null;

	//just a boolean flag that i use for enabling
	//and disabling buttons depending on whether the program
	//is connected to a serial port or not
	private boolean bConnected = false;

	//the timeout value for connecting with the port
	final static int TIMEOUT = 2000;

	//Some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;
	final static int START_BYTE = 91; // '['
	final static int STOP_BYTE = 93; // ']'
	final static int ASCII_VALUE_OF_ZERO = 48;

	//a string for recording what goes on in the program
	//this string is written to the GUI
	String logText = "";

	boolean dataPacketFlag = false;
	private int byteCounter = 0;
	byte netText[] = new byte[200];

	public int numRxD = 0;
	public int numTxD = 0;

	public boolean flagTx		= false;
	public boolean flagACKTx 	= false;
	public boolean flagEndData	= false;
	public boolean flagACKEnd	= false;

	public final String msgTx		= "[";
	public final String msgACKTx	= "<";
	public final String msgEndData 	= "]";
	public final String msgACKEnd	= ">";
	public final String msgACKFinal	= "?";

	public final byte byteTx		= singleStringToBytesASCII(msgTx);
	public final byte byteACKTx		= singleStringToBytesASCII(msgACKTx);
	public final byte byteEndData	= singleStringToBytesASCII(msgEndData);
	public final byte byteACKEnd 	= singleStringToBytesASCII(msgACKEnd);
	public final byte byteACKFinal	= singleStringToBytesASCII(msgACKFinal);

	public boolean sendingData = false;
	private long startTime = 0;

	private float numBytesRxdWrong = 0;
	public final float numBytesSent = 36;

	private boolean pckStartFlag = false;

	/*private int codeCnter = 6;
	private final String keyCode = "[SEND]";*/

	public SerialPortManager(Main main){
		this.main = main;
	}

	public void searchForPorts(){
		ports = CommPortIdentifier.getPortIdentifiers();

		if (!ports.hasMoreElements()) {
			main.gui.cboxPorts.addItem("No Serial Ports");
		}
		
		while (ports.hasMoreElements()) {
			CommPortIdentifier currentPort = (CommPortIdentifier) ports.nextElement();
			if (currentPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				main.gui.cboxPorts.addItem(currentPort.getName());
				portMap.put(currentPort.getName(), currentPort);
			} 
		}
	}

	public void connect(){
		String selectedPort = (String)main.gui.cboxPorts.getSelectedItem();
		selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

		CommPort commPort = null;

		try {
			commPort = selectedPortIdentifier.open("UWSN_ControlPanel", TIMEOUT);
			serialPort = (SerialPort)commPort;
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			setConnected(true);

			logText = selectedPort + " opened successfully!";
			main.gui.textMsgArea.setForeground(Color.black);
			main.gui.textMsgArea.append(logText + "\n");

			main.gui.toggleControls();

		} catch (PortInUseException e) {
			logText = selectedPort + " is in use. (" +e.toString() + ")";

			main.gui.textMsgArea.setForeground(Color.red);
			main.gui.textMsgArea.append(logText + "\n");
		} catch (Exception e) {
			logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";

			main.gui.textMsgArea.setForeground(Color.red);
			main.gui.textMsgArea.append(logText + "\n");
		}
	}

	public boolean initIOStream(){

		boolean successful = false;

		try {
			input 	= serialPort.getInputStream();
			output 	= serialPort.getOutputStream();

			successful = true;
			return successful;

		} catch (IOException e) {
			logText = "I/O Streams failed to open. (" + e.toString() + ")";
			main.gui.textMsgArea.setForeground(Color.red);
			main.gui.textMsgArea.append(logText + "\n");
			return successful;
		}
	}

	public void initListener(){

		try {
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (TooManyListenersException e) {
			logText = "Too many listeners. (" + e.toString() + ")";
			main.gui.textMsgArea.setForeground(Color.red);
			main.gui.textMsgArea.append(logText + "\n");
		}		
	}

	public void disconnect(){

		try {
			sendingData = true;
			serialPort.removeEventListener();
			serialPort.close();
			input.close();
			output.close();

			setConnected(false);
			main.gui.toggleControls();

			logText = "Disconnected.";
			main.gui.textMsgArea.setForeground(Color.red);
			main.gui.textMsgArea.append(logText + "\n");


		} catch (Exception e) {
			logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
			main.gui.textMsgArea.setForeground(Color.red);
			main.gui.textMsgArea.append(logText + "\n");
			//e.printStackTrace();
		}
	}

	public void serialEvent(SerialPortEvent spe) {

		if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//If one byte of data came in
			while (!sendingData) {
				try {
					if (sendingData) {
						break;
					}
					byte charVal = (byte) input.read();

					if (charVal != -1 && (System.currentTimeMillis() - startTime) > 100 ) { //To avoid "listening(saving)" to own's message.
						if (main.gui.isExpOneSelected()) {
							rxExperOne(charVal);
						} 
						else if (main.gui.isExpTwoSelected()) {
							rxExperTwo(charVal);
						} else if (main.gui.isExpThreeSelected()) {
							rxExperThree(charVal);
						}

						/*String str = new String(new byte[] { (byte) charVal });
						if (charVal == '!') { //byteCounter > 36
							window.textInputArea.append("\n");
							System.out.print("\n");
							refreshBERValue();
							byteCounter = 0;
						} else if (charVal == '>') {
							window.textInputArea.append(">");
							byteCounter = 0;
							window.textMsgArea.append("ACK Msg from Bot_B Rxd!\n");
						} else {
							netText[byteCounter++] = charVal;
							window.textInputArea.append(str);
							//System.out.print(new String(netText));
						}*/
					}

				} catch (Exception e) {
					logText = "Failed to read data." + "(" + e.toString() + ")";
					main.gui.textInputArea.setForeground(Color.red);
					main.gui.textInputArea.append(logText + "\n");
					System.err.println(e.toString());
				}
			}
		}
	}

	public void rxExperOne(byte val){
		String str = new String(new byte[] { (byte) val });
		if (val == '!') { 
			main.gui.textInputArea.append("\n");
			System.out.print("\n");
			refreshBERValue();
			byteCounter = 0;
		}  else {
			netText[byteCounter++] = val;
			main.gui.textInputArea.append(str);
			//System.out.print(new String(netText));
		}

	}

	public void rxExperTwo(byte val){
		String str = new String(new byte[] { (byte) val });

		if(!pckStartFlag && val == '<'){
			pckStartFlag = true;
			byteCounter = 0;
		}
		else if (pckStartFlag && val != '>') {
			netText[byteCounter++] = val;
			main.gui.textInputArea.append(str);
			main.fileLogger.WriteData(netText);
		}
		else if (pckStartFlag && val == '>') {
			pckStartFlag = false;
			byteCounter = 0;
			main.gui.textInputArea.append("\n");
		}

		/*if (val == '>') { 
			window.textInputArea.append("\n");
			System.out.print("\n");
			refreshBERValue();
			byteCounter = 0;
		} else if (val == '>') {
			window.textInputArea.append(">");
			byteCounter = 0;
			window.textMsgArea.append("ACK Msg from Bot_B Rxd!\n");
		} else {
			netText[byteCounter++] = val;
			window.textInputArea.append(str);
			//System.out.print(new String(netText));
		}*/
	}

	public void rxExperThree(byte val){
		int[] msgToChildren = NetworkProtocol.msgToChildren;
		String str = new String(new byte[] { (byte) val });

		if (dataPacketFlag == false && val == START_BYTE) { //Stores start byte
			dataPacketFlag = true;
			netText[byteCounter++] = val;
			main.gui.textInputArea.append(str);
		}

		else if (dataPacketFlag == true  && val != STOP_BYTE) { //Stores actual data
			main.gui.textInputArea.append(logText);
			netText[byteCounter++] = val;
			main.fileLogger.WriteData(netText);
		}

		else if (dataPacketFlag == true && val == STOP_BYTE) { //Stores stop byte
			dataPacketFlag = false;

			if (netText[1] != msgToChildren[0]) { //botID parent...						
				parseChildMsg(byteCounter);	
				printChildMsg(byteCounter);
				NetworkProtocol.setSuccessPcktRxd(true);
				numRxD++;
			} 

			main.gui.textInputArea.append(str);
			main.gui.textInputArea.append("\n");

			byteCounter = 0;
		}

	}

	public void refreshBERValue(){ //have to include the cases when sent and no response in calculation
		float result = 0;
		numBytesRxdWrong = 0;

		for (int i = 0; i < numBytesSent; i++) {
			//System.out.println("Orig: " + (byte) window.testMsg.charAt(i)  + " Rxd: " + netText[i]);
			if ( ((byte) main.gui.testMsg.charAt(i)) != netText[i]) {
				numBytesRxdWrong++;
			}
		}
		result = (float) ((numBytesRxdWrong/numBytesSent)*100);
		System.out.println(numBytesRxdWrong + " " + numBytesSent + " " + result);
		main.gui.lblBerValue.setText("Bit Error Rate: "+result+ "%");
		//window.threadManager.setWriteDataToFile(true);
		main.fileLogger.WriteData(netText);
	}

	public static byte[] stringToBytesASCII(String str) {
		byte[] b = new byte[str.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) str.charAt(i);
		}
		return b;
	}

	public static byte singleStringToBytesASCII(String str){
		return (byte) str.charAt(0);
	}

	public void sendData(String dataToSend){

		byte[] dataBytesOut = stringToBytesASCII(dataToSend);

		try {
			System.out.print("Data Sent: ");

			//window.textOutputArea.append("TxD-");

			for (int i = 0; i < dataBytesOut.length; i++) {
				output.write(dataBytesOut[i]);
				main.gui.textOutputArea.append(new String(new byte[] {(byte) dataBytesOut[i]}));
				System.out.print(dataBytesOut[i]);
			}

			output.flush();
			numTxD++;

			main.gui.textOutputArea.append("\n");
			System.out.print("\n");

		} catch (Exception e) {
			logText = "Failed to write data. (" + e.toString() + ")";
			main.gui.textOutputArea.setForeground(Color.red);
			main.gui.textOutputArea.append(logText + "\n");
		}

		startTime = System.currentTimeMillis();
	}
	
	public void sendData(int botID, int parent, int timeSlot){//, int packetSubTree){

		main.networkProtocol.createDataPacket(botID, parent, timeSlot);//, packetSubTree);
		int[] msgToChildren = NetworkProtocol.msgToChildren;

		try {
			output.write(START_BYTE);
			System.out.print("Data Sent: [");
			
			main.gui.textOutputArea.append("[");

			for (int i = 0; i < msgToChildren.length; i++) {
				output.write(msgToChildren[i]);
				main.gui.textOutputArea.append(new String(new byte[] {(byte) msgToChildren[i]}));
				System.out.print(new String(new byte[] {(byte) msgToChildren[i]}));
			}

			output.write(STOP_BYTE); 
			output.flush();
			numTxD++;
			
			main.gui.textOutputArea.append("]" + "\n");
			
			System.out.print(new String(new byte[] {STOP_BYTE}) + "\n");

		} catch (Exception e) {
			logText = "Failed to write data. (" + e.toString() + ")";
			main.gui.textOutputArea.setForeground(Color.red);
			main.gui.textOutputArea.append(logText + "\n");
			//e.printStackTrace();
		}
	}

	/*public void sendData(String dataToSend){

		byte[] dataBytesOut = stringToBytesASCII(dataToSend);

		try {
			System.out.print("Data Sent: [");

			window.textOutputArea.append("TxD-");

			for (int i = 0; i < dataBytesOut.length; i++) {
				output.write(dataBytesOut[i]);
				window.textOutputArea.append(new String(new byte[] {(byte) dataBytesOut[i]}));
				System.out.print(dataBytesOut[i]);
			}

			output.flush();
			numTxD++;

			window.textOutputArea.append("\n");
			System.out.print("\n");

		} catch (Exception e) {
			logText = "Failed to write data. (" + e.toString() + ")";
			window.textOutputArea.setForeground(Color.red);
			window.textOutputArea.append(logText + "\n");
		}
	}*/

	public void parseChildMsg(int byteCounter){
		int cnter = 0;
		int[] tmp = new int[2];
		int packetBotID = -1;
		int packetParent = -1;
		int packetTimeSlot = -1;
		
		for (int i = 1; i < byteCounter; i++) {

			if (i == 1) {
				packetBotID = Integer.parseInt(new String(new byte[] {netText[i]})); //(int) netText[i] & 0xff;
				//window.logAreaText.append("{" + NetworkProtocol.packetBotId + " " + Integer.parseInt(new String(new byte[] {netText[i]})) + "}");
			} else if (i == 2) {
				packetParent = (int) netText[i] & 0xff;
			} else if (i == 3 || i == 4) {
				tmp[cnter++] = Integer.parseInt(new String(new byte[] {netText[i]})); //new String(new byte[] {netText[i]}); //new String(new byte[] {netText[i]});//String.valueOf((int) netText[i] & 0xff);
				//window.logAreaText.append("{" + cnter+ " " + netText[i] + " " + Integer.parseInt(new String(new byte[] {netText[i]})) + "}");

				if (cnter == 2) {
					packetTimeSlot = tmp[0]*10 + tmp[1];
					//window.timeSlotValueLabel.setText(NetworkProtocol.myTimeSlotCounter + " / " + packetTimeSlot);
					cnter = 0;
				}
			}
		}
		NetworkProtocol.queue.add(new Package(packetBotID, packetParent, packetTimeSlot));
	}

	public void printChildMsg(int byteCounter){

		for (int i = 1; i < byteCounter; i++) {

			if (i == 1 || i == 2 || i == 11) {				
				main.gui.textParsedArea.append(new String(new byte[] {netText[i]}) + " ");
			} 

			else if (i >= 3 && i <= 10 ) {
				if (i == 3) {
					main.gui.textParsedArea.append(new String(new byte[] {netText[i]}));
				}
				else if (i == 4) {
					main.gui.textParsedArea.append(new String(new byte[] {netText[i]}) + "-");
				} else {
					if (i % 2 == 0) {//if even			
						main.gui.textParsedArea.append(new String(new byte[] {netText[i]}) + ": "); 
					} else {
						main.gui.textParsedArea.append(new String(new byte[] {netText[i]}));
					}
				}

			}

			else if (i >= 12) {
				if ((i - 12 )% 9 == 0) {
					main.gui.textParsedArea.append(new String(new byte[] {netText[i]}) + "-");
				} else {
					if (i % 2 == 0) {					
						main.gui.textParsedArea.append(new String(new byte[] {netText[i]}) + "::"); 
					} else {
						main.gui.textParsedArea.append(new String(new byte[] {netText[i]}));
					}
				}
			}
		}
		main.gui.textParsedArea.append("\n");
	}
	
	public void printChildMsg2(int byteCounter){

		for (int i = 0; i < byteCounter; i++) {
			main.gui.textParsedArea.append(new String(new byte[] {netText[i]}) + " ");
		}
		main.gui.textInputArea.append("\n");
	}

	final public boolean getConnectionStatus()
	{
		return bConnected;
	}

	public void setConnected(boolean bConnected)
	{
		this.bConnected = bConnected;
	}

	public int getNumRxD() {
		return numRxD;
	}

	public int getNumTxD() {
		return numTxD;
	}
}
