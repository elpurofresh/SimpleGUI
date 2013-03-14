package guiMain;

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

public class SerialPortManager implements SerialPortEventListener{

	//passed from main GUI
	GuiMain window = null;

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

	//some ascii values for for certain things
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

	public SerialPortManager(GuiMain window){
		this.window = window;
	}

	public void searchForPorts(){
		ports = CommPortIdentifier.getPortIdentifiers();

		while (ports.hasMoreElements()) {
			CommPortIdentifier currentPort = (CommPortIdentifier) ports.nextElement();
			if (currentPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				window.cboxPorts.addItem(currentPort.getName());
				portMap.put(currentPort.getName(), currentPort);
			}
		}
	}

	public void connect(){
		String selectedPort = (String)window.cboxPorts.getSelectedItem();
		selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

		CommPort commPort = null;

		try {
			commPort = selectedPortIdentifier.open("UWSN_ControlPanel", TIMEOUT);
			serialPort = (SerialPort)commPort;
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			setConnected(true);

			logText = selectedPort + " opened successfully!";
			window.textMsgArea.setForeground(Color.black);
			window.textMsgArea.append(logText + "\n");

			window.toggleControls();

		} catch (PortInUseException e) {
			logText = selectedPort + " is in use. (" +e.toString() + ")";

			window.textMsgArea.setForeground(Color.red);
			window.textMsgArea.append(logText + "\n");
		} catch (Exception e) {
			logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";

			window.textMsgArea.setForeground(Color.red);
			window.textMsgArea.append(logText + "\n");
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
			window.textMsgArea.setForeground(Color.red);
			window.textMsgArea.append(logText + "\n");
			return successful;
		}
	}

	public void initListener(){

		try {
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (TooManyListenersException e) {
			logText = "Too many listeners. (" + e.toString() + ")";
			window.textMsgArea.setForeground(Color.red);
			window.textMsgArea.append(logText + "\n");
		}		
	}

	public void disconnect(){
		try {
			serialPort.removeEventListener();
			serialPort.close();
			input.close();
			output.close();

			setConnected(false);
			window.toggleControls();

			logText = "Disconnected.";
			window.textMsgArea.setForeground(Color.red);
			window.textMsgArea.append(logText + "\n");


		} catch (Exception e) {
			logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
			window.textMsgArea.setForeground(Color.red);
			window.textMsgArea.append(logText + "\n");
			//e.printStackTrace();
		}
	}

	final public boolean getConnectionStatus()
	{
		return bConnected;
	}

	public void setConnected(boolean bConnected)
	{
		this.bConnected = bConnected;
	}

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

	public void serialEvent(SerialPortEvent spe) {

		if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//If one byte of data came in

			try {
				byte charVal = (byte)input.read();
				String str = new String(new byte[] {(byte) charVal});
				window.textInputArea.append(str);
				System.out.println(str);

				//Receiver
				if (charVal == byteTx && !window.protocolManager.isFlagACKTx()) {	//Rxd Send Request
					window.protocolManager.setFlagACKTx(true);
					window.protocolManager.setFlagRxMode(true);
					sendData(msgACKTx);
				}	
				
				if (charVal != byteEndData && window.protocolManager.isFlagACKTx()) { //Store data
					//netText[byteCounter++] = charVal;
					window.textInputArea.append(str);
					
					/*if (byteCounter > 38) {
						byteCounter = 0;					
						window.textInputArea.append("\n");
					}*/
				}
				
				if (charVal == byteEndData && window.protocolManager.isFlagACKTx()) {				//Rxd endData
					window.protocolManager.setFlagACKTx(false);
					sendData(msgACKEnd);
				}
				
				//Transmitter
				if (charVal == byteACKTx && !window.protocolManager.isFlagACKTx()) {	//Rxd Send Request
					window.protocolManager.setFlagACKTx(true);
					window.protocolManager.setFlagRxMode(false);
					sendData(window.textOutputTest.getText());
					sendData(msgEndData);
				}

				if (charVal == byteACKEnd && window.protocolManager.isFlagACKTx()) {	//Rxd ACKSend
					window.protocolManager.setFlagACKTx(false);
					window.protocolManager.setFlagACKFinal(true);
					sendData(msgACKEnd);
				}
				
				if (charVal != byteACKFinal && window.protocolManager.isFlagACKFinal()) {
					netText[byteCounter++] = charVal;
					window.textInputArea.append(str);
					
					if (byteCounter > 38) {
						byteCounter = 0;					
						window.textInputArea.append("\n");
					}
				}
				
				if (charVal == byteACKFinal && window.protocolManager.isFlagACKFinal()) {
					window.protocolManager.setFlagACKFinal(false);
				}

			} catch (Exception e) {
				logText = "Failed to read data." + "(" + e.toString() + ")";
				window.textInputArea.setForeground(Color.red);
				window.textInputArea.append(logText + "\n");
				System.err.println(e.toString());
			}
		}
	}

	/*public void serialEvent(SerialPortEvent spe) {

		if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//If one byte of data came in


			try {
				byte charVal = (byte)input.read();

				if (charVal == START_BYTE && dataPacketFlag == false) { //Stores start byte
					dataPacketFlag = true;

					netText[byteCounter++] = charVal;

					window.textInputArea.append("RxD-");
					logText = new String(new byte[] {charVal});
					window.textInputArea.append(logText);
				}

				else if (charVal != STOP_BYTE && dataPacketFlag == true) { //Stores actual data
					logText = new String(new byte[] {charVal});
					window.textInputArea.append(logText);

					netText[byteCounter++] = charVal;					
				}

				else if (charVal == STOP_BYTE && dataPacketFlag == true) { //Stores stop byte
					dataPacketFlag = false;

					//if (netText[1] != msgToChildren[0]) { //botID parent...						
					printChildMsg(byteCounter);
					numRxD++;
					//} 

					logText = new String(new byte[] {charVal});
					window.textInputArea.append(logText);
					window.textInputArea.append("\n");

					byteCounter = 0;
				}

			} catch (Exception e) {
				logText = "Failed to read data." + "(" + e.toString() + ")";
				window.textInputArea.setForeground(Color.red);
				window.textInputArea.append(logText + "\n");
				System.err.println(e.toString());
			}
		}
	}*/

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

		//int[] msgToChildren = new int[20]; 
		//String test = "012903405sdfgsdgsdjkg";
		//byte[] dataBytes = new byte[dataToSend.length()];
		//dataBytes = dataToSend.getBytes();

		byte[] dataBytesOut = stringToBytesASCII(dataToSend);

		try {
			//output.write(START_BYTE);
			System.out.print("Data Sent: [");

			window.textOutputArea.append("TxD-");
			//logText = new String(new byte[] {(byte) START_BYTE});
			//window.textOutputArea.append(logText);

			//output.write(dataToSend.getBytes());
			//window.textOutputArea.append(new String(dataToSend.getBytes()));
			//System.out.print(dataToSend);
			for (int i = 0; i < dataBytesOut.length; i++) {
				output.write(dataBytesOut[i]);
				window.textOutputArea.append(new String(new byte[] {(byte) dataBytesOut[i]}));
				//System.out.print(new String(new byte[] {(byte) dataOut[i]}));
				System.out.print(dataBytesOut[i]);
				//window.textOutputArea.append(new String(new byte[] {(byte) msgToChildren[i]}));
				//System.out.print(new String(new byte[] {(byte) msgToChildren[i]}));
			}

			//output.write(STOP_BYTE); 
			output.flush();
			numTxD++;

			//logText = new String(new byte[] {(byte) STOP_BYTE});
			//window.textOutputArea.append(logText + "\n");
			//System.out.print(new String(new byte[] {STOP_BYTE}) + "\n");
			window.textOutputArea.append("\n");
			System.out.print("\n");

		} catch (Exception e) {
			logText = "Failed to write data. (" + e.toString() + ")";
			window.textOutputArea.setForeground(Color.red);
			window.textOutputArea.append(logText + "\n");
		}
	}

	public void printChildMsg(int byteCounter){

		for (int i = 0; i < byteCounter; i++) {
			window.textInputArea.append(new String(new byte[] {netText[i]}) + " ");
		}
		window.textInputArea.append("\n");
	}

	public int getNumRxD() {
		return numRxD;
	}

	public int getNumTxD() {
		return numTxD;
	}


}
