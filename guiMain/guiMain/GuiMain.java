package guiMain;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class GuiMain {

	SerialPortManager serialPortManager = null;
	ProtocolControl protocolManager = null;
	//Thread protocolThread = null;


	private JFrame frame;

	JLabel lblMainTitle = new JLabel("Underwater Communications");
	JLabel lblDataOut = new JLabel("Data Out");
	JLabel lblDataIn = new JLabel("Data In");
	JLabel lblSelectComm = new JLabel("Select Comm ->");
	JComboBox cboxPorts = new JComboBox();
	JButton btnConnect = new JButton("Connect");
	JButton btnDisconnect = new JButton("Disconnect");
	JLabel lblStringOut = new JLabel("String out");
	JTextField textInterval = new JTextField();
	JTextField textOutputTest = new JTextField();
	JLabel lblArrow = new JLabel("=>");
	JScrollPane scrollPaneOutput = new JScrollPane();
	JTextArea textOutputArea = new JTextArea();
	JLabel lblInterval = new JLabel("Interval (sec):");
	JButton btnStartComm = new JButton("START COMM");
	JButton btnStopComm = new JButton("STOP COMM");
	JLabel lblBer = new JLabel("BER: ");
	JLabel lblBerValue = new JLabel("0.0 %");
	JTextArea textInputArea = new JTextArea();
	JScrollPane scrollPaneInput = new JScrollPane();
	JLabel lblControlPanel = new JLabel("Control Panel");
	JTextArea textMsgArea = new JTextArea();
	JScrollPane scrollPaneMsg = new JScrollPane();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiMain window = new GuiMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiMain() {
		initialize();
		serialPortManager = new SerialPortManager(this);
		serialPortManager.searchForPorts();
		protocolManager = new ProtocolControl(this);
		//protocolThread = new Thread(protocolManager, "Protocol_Manager");
		//protocolManager.setRunCondition(true);
		//System.out.println("GOT IN1");

	}

	public void toggleControls(){
		if (serialPortManager.getConnectionStatus()) {

			cboxPorts.setEnabled(false);
			btnConnect.setEnabled(false);

			btnDisconnect.setEnabled(true);
			btnStartComm.setEnabled(true);
			btnStopComm.setEnabled(true);
			textOutputTest.setEnabled(true);
			textInterval.setEnabled(true);
		}
		else {
			cboxPorts.setEnabled(true);
			btnConnect.setEnabled(true);

			btnDisconnect.setEnabled(false);
			btnStartComm.setEnabled(false);
			btnStopComm.setEnabled(false);
			textOutputTest.setEnabled(false);
			textInterval.setEnabled(false);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1037, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{178, 194, 0, 122, 169, 152, 93, 26};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 26, 0, 23, 31, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);


		GridBagConstraints gbc_lblUnderwaterCommunications = new GridBagConstraints();
		gbc_lblUnderwaterCommunications.gridwidth = 8;
		gbc_lblUnderwaterCommunications.insets = new Insets(0, 0, 5, 5);
		gbc_lblUnderwaterCommunications.gridx = 0;
		gbc_lblUnderwaterCommunications.gridy = 0;
		frame.getContentPane().add(lblMainTitle, gbc_lblUnderwaterCommunications);

		GridBagConstraints gbc_lblControlPanel = new GridBagConstraints();
		gbc_lblControlPanel.gridwidth = 2;
		gbc_lblControlPanel.insets = new Insets(0, 0, 5, 5);
		gbc_lblControlPanel.gridx = 0;
		gbc_lblControlPanel.gridy = 1;
		frame.getContentPane().add(lblControlPanel, gbc_lblControlPanel);


		GridBagConstraints gbc_lblDataOut = new GridBagConstraints();
		gbc_lblDataOut.gridwidth = 2;
		gbc_lblDataOut.insets = new Insets(0, 0, 5, 5);
		gbc_lblDataOut.gridx = 3;
		gbc_lblDataOut.gridy = 1;
		frame.getContentPane().add(lblDataOut, gbc_lblDataOut);
		frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblDataOut, lblDataIn, textOutputTest}));


		GridBagConstraints gbc_lblDataIn = new GridBagConstraints();
		gbc_lblDataIn.gridwidth = 2;
		gbc_lblDataIn.insets = new Insets(0, 0, 5, 5);
		gbc_lblDataIn.gridx = 5;
		gbc_lblDataIn.gridy = 1;
		frame.getContentPane().add(lblDataIn, gbc_lblDataIn);


		GridBagConstraints gbc_lblSelectComm = new GridBagConstraints();
		gbc_lblSelectComm.anchor = GridBagConstraints.EAST;
		gbc_lblSelectComm.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectComm.gridx = 0;
		gbc_lblSelectComm.gridy = 2;
		frame.getContentPane().add(lblSelectComm, gbc_lblSelectComm);


		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 2;
		frame.getContentPane().add(cboxPorts, gbc_comboBox);


		GridBagConstraints gbc_scrollPaneInput = new GridBagConstraints();
		gbc_scrollPaneInput.gridwidth = 3;
		gbc_scrollPaneInput.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneInput.gridheight = 6;
		gbc_scrollPaneInput.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneInput.gridx = 5;
		gbc_scrollPaneInput.gridy = 2;
		frame.getContentPane().add(scrollPaneInput, gbc_scrollPaneInput);
		textInputArea.setRows(9);
		textInputArea.setColumns(10);
		scrollPaneInput.setViewportView(textInputArea);

		GridBagConstraints gbc_scrollPaneMsg = new GridBagConstraints();
		gbc_scrollPaneMsg.gridheight = 2;
		gbc_scrollPaneMsg.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneMsg.gridwidth = 2;
		gbc_scrollPaneMsg.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneMsg.gridx = 0;
		gbc_scrollPaneMsg.gridy = 3;
		frame.getContentPane().add(scrollPaneMsg, gbc_scrollPaneMsg);
		textMsgArea.setRows(2);
		textMsgArea.setColumns(10);
		scrollPaneMsg.setViewportView(textMsgArea);

		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridx = 0;
		gbc_btnConnect.gridy = 5;
		frame.getContentPane().add(btnConnect, gbc_btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				serialPortManager.connect();
				if (serialPortManager.getConnectionStatus() == true)
				{
					if (serialPortManager.initIOStream() == true)
					{
						serialPortManager.initListener();
					}
				}
				textOutputTest.setText("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			}
		});

		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnDisconnect.gridx = 1;
		gbc_btnDisconnect.gridy = 5;
		frame.getContentPane().add(btnDisconnect, gbc_btnDisconnect);
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serialPortManager.disconnect();
				textInputArea.setText("");
				textOutputArea.setText("");
			}
		});


		GridBagConstraints gbc_lblStringOut = new GridBagConstraints();
		gbc_lblStringOut.insets = new Insets(0, 0, 5, 5);
		gbc_lblStringOut.gridx = 0;
		gbc_lblStringOut.gridy = 6;
		frame.getContentPane().add(lblStringOut, gbc_lblStringOut);


		textOutputTest.setText("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		GridBagConstraints gbc_outputText = new GridBagConstraints();
		gbc_outputText.fill = GridBagConstraints.HORIZONTAL;
		gbc_outputText.insets = new Insets(0, 0, 5, 5);
		gbc_outputText.gridx = 1;
		gbc_outputText.gridy = 6;
		frame.getContentPane().add(textOutputTest, gbc_outputText);
		textOutputTest.setColumns(10);
		textOutputTest.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent evt) {
				//	if (evt.getSource() == textOutputTest) {
				textOutputTest.setText("");
				//	}
			}
		});


		GridBagConstraints gbc_lblArrow = new GridBagConstraints();
		gbc_lblArrow.insets = new Insets(0, 0, 5, 5);
		gbc_lblArrow.gridx = 2;
		gbc_lblArrow.gridy = 6;
		frame.getContentPane().add(lblArrow, gbc_lblArrow);


		GridBagConstraints gbc_scrollPaneOutput = new GridBagConstraints();
		gbc_scrollPaneOutput.gridwidth = 2;
		gbc_scrollPaneOutput.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneOutput.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneOutput.gridheight = 6;
		gbc_scrollPaneOutput.gridx = 3;
		gbc_scrollPaneOutput.gridy = 2;
		frame.getContentPane().add(scrollPaneOutput, gbc_scrollPaneOutput);
		scrollPaneOutput.setViewportView(textOutputArea);
		textOutputArea.setRows(9);
		textOutputArea.setColumns(10);


		GridBagConstraints gbc_lblInterval = new GridBagConstraints();
		gbc_lblInterval.insets = new Insets(0, 0, 5, 5);
		gbc_lblInterval.gridx = 0;
		gbc_lblInterval.gridy = 7;
		frame.getContentPane().add(lblInterval, gbc_lblInterval);


		textInterval.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		textInterval.setText("0");
		GridBagConstraints gbc_textInterval = new GridBagConstraints();
		gbc_textInterval.insets = new Insets(0, 0, 5, 5);
		gbc_textInterval.fill = GridBagConstraints.HORIZONTAL;
		gbc_textInterval.gridx = 1;
		gbc_textInterval.gridy = 7;
		frame.getContentPane().add(textInterval, gbc_textInterval);
		textInterval.setColumns(10);


		btnStartComm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//serialPortManager.sendData(textOutputTest.getText());
				//protocolManager.startComm();
				//serialPortManager.sendData(serialPortManager.msgTx);
				serialPortManager.sendDataOneChar(textOutputTest.getText());
			}
		});
		GridBagConstraints gbc_btnStartComm = new GridBagConstraints();
		gbc_btnStartComm.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartComm.insets = new Insets(0, 0, 0, 5);
		gbc_btnStartComm.gridx = 0;
		gbc_btnStartComm.gridy = 8;
		frame.getContentPane().add(btnStartComm, gbc_btnStartComm);


		btnStopComm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnStopComm = new GridBagConstraints();
		gbc_btnStopComm.fill = GridBagConstraints.BOTH;
		gbc_btnStopComm.insets = new Insets(0, 0, 0, 5);
		gbc_btnStopComm.gridx = 1;
		gbc_btnStopComm.gridy = 8;
		frame.getContentPane().add(btnStopComm, gbc_btnStopComm);

		GridBagConstraints gbc_lblBertotalWrongtotal = new GridBagConstraints();
		gbc_lblBertotalWrongtotal.insets = new Insets(0, 0, 0, 5);
		gbc_lblBertotalWrongtotal.anchor = GridBagConstraints.WEST;
		gbc_lblBertotalWrongtotal.gridx = 3;
		gbc_lblBertotalWrongtotal.gridy = 8;
		frame.getContentPane().add(lblBer, gbc_lblBertotalWrongtotal);

		GridBagConstraints gbc_label_1BerValue = new GridBagConstraints();
		gbc_label_1BerValue.anchor = GridBagConstraints.WEST;
		gbc_label_1BerValue.insets = new Insets(0, 0, 0, 5);
		gbc_label_1BerValue.gridx = 4;
		gbc_label_1BerValue.gridy = 8;
		frame.getContentPane().add(lblBerValue, gbc_label_1BerValue);
	}

}
