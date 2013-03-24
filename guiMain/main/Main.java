package main;


import java.awt.Color;

import javax.swing.border.LineBorder;

import backend.FileReadWrite;
import backend.GraphicalRep;
import backend.NetworkProtocol;
import backend.SerialPortManager;
import backend.ThreadManager;

public class Main {
	
	SerialPortManager serialPortManager 	= null;
	//ProtocolControl protocolManager 		= null;
	NetworkProtocol networkProtocol			= null;
	
	Thread threadMainRx						= null;
	Thread threadMainTx						= null;
	Thread threadMainTx2					= null;
	Thread threadProtocol					= null;
	ThreadManager threadManager 			= null;
	FileReadWrite fileLogger 				= null;
	ExperimentOne experOne					= null;
	ExperimentTwo experTwo					= null;
	
	public void Main(){
		
		serialPortManager = new SerialPortManager(this);
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
		// TODO Auto-generated method stub

	}

}
