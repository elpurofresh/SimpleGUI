package main;

import gui.Gui;
import backend.FileReadWrite;
import backend.NetworkProtocol;
import backend.SerialPortManager;
import backend.ThreadManager;

public class Main {
	
	public Gui gui								= null;
	public SerialPortManager serialPortManager 	= null;
	public NetworkProtocol networkProtocol		= null;
	public Thread threadMainRx					= null;
	public Thread threadMainTx					= null;
	public Thread threadMainTx2					= null;
	public Thread threadProtocol				= null;
	public ThreadManager threadManager 			= null;
	public FileReadWrite fileLogger 			= null;
	public ExperimentOne experOne				= null;
	public ExperimentTwo experTwo				= null;
	
	public Main(){
		
		serialPortManager = new SerialPortManager(this);
		gui = new Gui(this);
		serialPortManager.searchForPorts();
		networkProtocol = new NetworkProtocol(this);
		
		//protocolManager = new ProtocolControl(this);
		//protocolThread = new Thread(protocolManager, "Protocol_Manager");
		//protocolManager.setRunCondition(true);
		//System.out.println("GOT IN1");
		fileLogger = new FileReadWrite(this);
		threadManager = new ThreadManager(this);
		//threadMain = new Thread(threadManager, "Thread_Manager");
		//threadMain.start();
		
		experOne = new ExperimentOne(this);
		experTwo = new ExperimentTwo(this);
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}

}
