package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import main.Main;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import pinger.EventClass;
import pinger.EventClassListener;
import pinger.EventSource;

public class Gui implements EventClassListener{

	Main main = null;
	EventSource event = null;


	private JFrame frame;

	public JLabel lblMainTitle = new JLabel("Underwater Communications");
	public JLabel lblDataOut = new JLabel("Data Out");
	public JLabel lblDataIn = new JLabel("Data In");
	public JLabel lblSelectComm = new JLabel("Select Comm ->");
	public JComboBox cboxPorts = new JComboBox();
	public JButton btnConnect = new JButton("Connect");
	public JButton btnDisconnect = new JButton("Disconnect");
	public JLabel lblStringOut = new JLabel("String out");
	public JTextField textInterval = new JTextField();
	public JTextField textOutputTest = new JTextField();
	public JLabel lblArrow = new JLabel("=>");
	public JScrollPane scrollPaneOutput = new JScrollPane();
	public JTextArea textOutputArea = new JTextArea();
	public JLabel lblInterval = new JLabel("Interval (sec):");
	public JButton btnStartComm = new JButton("START COMM");
	public JButton btnStopComm = new JButton("STOP COMM");
	public JLabel lblBerValue = new JLabel("Bit Error Rate: 0.0 %");
	public JTextArea textInputArea = new JTextArea();
	public JScrollPane scrollPaneInput = new JScrollPane();
	public JLabel lblControlPanel = new JLabel("Control Panel");
	public JTextArea textMsgArea = new JTextArea();
	public JScrollPane scrollPaneMsg = new JScrollPane();

	public final String testMsg = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final JLabel lblExperiment = new JLabel("Experiment #");
	public final JPanel panelExperiment = new JPanel();
	public final JRadioButton rdbtnExp1 = new JRadioButton("One");
	public final JRadioButton rdbtnExp2 = new JRadioButton("Two");
	public final JRadioButton rdbtnExp3 = new JRadioButton("Three");
	public final JLabel lblParsedData = new JLabel("Parsed Data");
	public JTextArea textParsedArea = new JTextArea();
	public final JScrollPane scrollPaneParsed = new JScrollPane();
	//private final JPanel panelGraph = new JPanel();
	public final JLabel lblNetGraphRep = new JLabel("Network Graphical Representation");

	public boolean startComm 			= false;
	public boolean expOneSelected 		= false;
	public boolean expTwoSelected 		= false;
	public boolean expThreeSelected 	= false;
	public final JPanel panelInterval 	= new JPanel();
	public final JLabel lblNumberOfTests = new JLabel("Number of Tests: 0");
	public final JPanel panelStringOut = new JPanel();

	public GraphicalRep animation 					= null;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
				//	window.animation.setLocale(null);
					//window.animation.setVisible(true);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/


	/**
	 * Create the application.
	 */
	public Gui(Main main) {
		this.main = main;
		event = new EventSource();

		animation = new GraphicalRep(main);
		animation.setForeground(new Color(0, 0, 0));
		animation.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		animation.setBackground(Color.LIGHT_GRAY);
		initialize();
		toggleControls();
	}

	public void toggleControls(){
		if (main.serialPortManager.getConnectionStatus()) {

			cboxPorts.setEnabled(false);
			btnConnect.setEnabled(false);

			btnDisconnect.setEnabled(true);
			btnStartComm.setEnabled(true);
			btnStopComm.setEnabled(true);
			textOutputTest.setEnabled(true);
			textInterval.setEnabled(true);
			rdbtnExp1.setEnabled(true);
			rdbtnExp1.setSelected(false);
			rdbtnExp2.setEnabled(true);
			rdbtnExp2.setSelected(false);
			rdbtnExp3.setEnabled(true);
			rdbtnExp3.setSelected(false);

		}
		else {
			cboxPorts.setEnabled(true);
			btnConnect.setEnabled(true);

			btnDisconnect.setEnabled(false);
			btnStartComm.setEnabled(false);
			btnStopComm.setEnabled(false);
			textOutputTest.setEnabled(false);
			textInterval.setEnabled(false);
			lblNumberOfTests.setText("Number of Tests: 0");

			rdbtnExp1.setEnabled(false);
			rdbtnExp1.setSelected(false);
			rdbtnExp2.setEnabled(false);
			rdbtnExp2.setSelected(false);
			rdbtnExp3.setEnabled(false);
			rdbtnExp3.setSelected(false);
			setExpOneSelected(false);
			setExpTwoSelected(false);
			setExpThreeSelected(false);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1207, 605);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{160, 80, 25, 200, 200, 200, 200};
		gridBagLayout.rowHeights = new int[]{10, 5, 1, 85, 0, 0, 0, 5, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);


		GridBagConstraints gbc_lblUnderwaterCommunications = new GridBagConstraints();
		gbc_lblUnderwaterCommunications.gridwidth = 7;
		gbc_lblUnderwaterCommunications.insets = new Insets(0, 0, 5, 0);
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


		GridBagConstraints gbc_lblDataIn = new GridBagConstraints();
		gbc_lblDataIn.gridwidth = 2;
		gbc_lblDataIn.insets = new Insets(0, 0, 5, 0);
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
		gbc_scrollPaneInput.gridwidth = 2;
		gbc_scrollPaneInput.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneInput.gridheight = 5;
		gbc_scrollPaneInput.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneInput.gridx = 5;
		gbc_scrollPaneInput.gridy = 2;
		frame.getContentPane().add(scrollPaneInput, gbc_scrollPaneInput);
		textInputArea.setRows(9);
		textInputArea.setColumns(10);
		scrollPaneInput.setViewportView(textInputArea);

		GridBagConstraints gbc_scrollPaneMsg = new GridBagConstraints();
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
		gbc_btnConnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridx = 0;
		gbc_btnConnect.gridy = 4;
		frame.getContentPane().add(btnConnect, gbc_btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				main.serialPortManager.connect();
				if (main.serialPortManager.getConnectionStatus() == true)
				{
					if (main.serialPortManager.initIOStream() == true)
					{
						main.serialPortManager.initListener();
					}
				}
				textOutputTest.setText(testMsg);
				animation.updateMasterNodePic("Idle");

				//Since a thread cannot be restarted we need to recreate the same object.
				main.threadMainRx = new Thread(main.threadManager, "Thread_Manager");
				main.threadMainRx.start();
				main.threadManager.runCondition = true;
			}
		});

		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnDisconnect.gridx = 1;
		gbc_btnDisconnect.gridy = 4;
		frame.getContentPane().add(btnDisconnect, gbc_btnDisconnect);

		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {



				main.serialPortManager.disconnect();
				textInputArea.setText("");
				textOutputArea.setText("");
				try {
					main.threadManager.runCondition = false;
					if (main.threadMainRx != null && main.threadMainRx.isAlive()) {
						main.threadMainRx.join();
					}
					if (main.threadMainTx != null && main.threadMainTx.isAlive()) {
						main.threadMainTx.join();
					}
					if (main.threadMainTx2 != null && main.threadMainTx2.isAlive()) {
						main.threadMainTx2.join();
					} 
					if (main.threadProtocol != null && main.threadProtocol.isAlive()) {
						main.threadProtocol.join();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});

		GridBagConstraints gbc_panelStringOut = new GridBagConstraints();
		gbc_panelStringOut.gridwidth = 2;
		gbc_panelStringOut.insets = new Insets(0, 0, 5, 5);
		gbc_panelStringOut.fill = GridBagConstraints.BOTH;
		gbc_panelStringOut.gridx = 0;
		gbc_panelStringOut.gridy = 5;
		frame.getContentPane().add(panelStringOut, gbc_panelStringOut);
		panelStringOut.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelStringOut.add(lblStringOut);
		frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblDataOut, lblDataIn, textOutputTest}));
		panelStringOut.add(textOutputTest);

		textOutputTest.setText(testMsg);
		textOutputTest.setColumns(25);
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
		gbc_lblArrow.gridy = 5;
		frame.getContentPane().add(lblArrow, gbc_lblArrow);

		GridBagConstraints gbc_scrollPaneOutput = new GridBagConstraints();
		gbc_scrollPaneOutput.gridwidth = 2;
		gbc_scrollPaneOutput.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneOutput.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneOutput.gridheight = 5;
		gbc_scrollPaneOutput.gridx = 3;
		gbc_scrollPaneOutput.gridy = 2;
		frame.getContentPane().add(scrollPaneOutput, gbc_scrollPaneOutput);
		scrollPaneOutput.setViewportView(textOutputArea);
		textOutputArea.setRows(9);
		textOutputArea.setColumns(10);

		GridBagConstraints gbc_panelInterval = new GridBagConstraints();
		gbc_panelInterval.insets = new Insets(0, 0, 5, 5);
		gbc_panelInterval.fill = GridBagConstraints.BOTH;
		gbc_panelInterval.gridx = 0;
		gbc_panelInterval.gridy = 6;
		frame.getContentPane().add(panelInterval, gbc_panelInterval);
		panelInterval.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelInterval.add(lblInterval);
		panelInterval.add(textInterval);


		textInterval.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		textInterval.setText("5");
		textInterval.setColumns(5);

		GridBagConstraints gbc_lblNumberOfTests = new GridBagConstraints();
		gbc_lblNumberOfTests.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfTests.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfTests.gridx = 1;
		gbc_lblNumberOfTests.gridy = 6;
		frame.getContentPane().add(lblNumberOfTests, gbc_lblNumberOfTests);

		GridBagConstraints gbc_lblExperiment = new GridBagConstraints();
		gbc_lblExperiment.insets = new Insets(0, 0, 5, 5);
		gbc_lblExperiment.gridx = 0;
		gbc_lblExperiment.gridy = 7;
		frame.getContentPane().add(lblExperiment, gbc_lblExperiment);

		GridBagConstraints gbc_panelExperiment = new GridBagConstraints();
		gbc_panelExperiment.insets = new Insets(0, 0, 5, 5);
		gbc_panelExperiment.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelExperiment.gridx = 1;
		gbc_panelExperiment.gridy = 7;
		frame.getContentPane().add(panelExperiment, gbc_panelExperiment);
		panelExperiment.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));


		rdbtnExp1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnExp1.setSelected(true);
				rdbtnExp2.setEnabled(false);
				rdbtnExp2.setSelected(false);
				rdbtnExp3.setEnabled(false);
				rdbtnExp3.setSelected(false);
				textMsgArea.append("Experiment 1 Selected: " + String.valueOf(rdbtnExp1.isSelected()) + '\n');	
			}
		});		
		panelExperiment.add(rdbtnExp1);

		rdbtnExp2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnExp1.setEnabled(false);
				rdbtnExp1.setSelected(false);
				rdbtnExp3.setEnabled(false);
				rdbtnExp3.setSelected(false);
				textMsgArea.append("Experiment 2 Selected: " + String.valueOf(rdbtnExp2.isSelected()) + '\n');
			}
		});
		panelExperiment.add(rdbtnExp2);

		rdbtnExp3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnExp1.setEnabled(false);
				rdbtnExp1.setSelected(false);
				rdbtnExp2.setEnabled(false);
				rdbtnExp2.setSelected(false);
				textMsgArea.append("Experiment 3 Selected: " + String.valueOf(rdbtnExp3.isSelected()) + '\n');
			}
		});		
		panelExperiment.add(rdbtnExp3);

		GridBagConstraints gbc_btnStartComm = new GridBagConstraints();
		gbc_btnStartComm.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartComm.insets = new Insets(0, 0, 5, 5);
		gbc_btnStartComm.gridx = 0;
		gbc_btnStartComm.gridy = 8;
		frame.getContentPane().add(btnStartComm, gbc_btnStartComm);
		btnStartComm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setStartComm(true);

				if (rdbtnExp1.isSelected()) {
					//serialPortManager.sendData(textOutputTest.getText());
					//protocolManager.startComm();
					//serialPortManager.sendData(serialPortManager.msgTx);
					setExpOneSelected(true);
					setExpTwoSelected(false);
					setExpThreeSelected(false);
					textMsgArea.append("Starting Experiment 1!!\n");
					//serialPortManager.sendData(textOutputTest.getText());		
					main.threadMainTx = new Thread(main.experOne, "Experiment_One");
					main.threadMainTx.start();

				} else if (rdbtnExp2.isSelected()) {
					setExpOneSelected(false);
					setExpTwoSelected(true);
					setExpThreeSelected(false);

					textMsgArea.append("Starting Experiment 2!!\n");
					main.threadMainTx2 = new Thread(main.experTwo, "Experiment_Two");
					main.threadMainTx2.start();

				} else if (rdbtnExp3.isSelected()) {
					setExpOneSelected(false);
					setExpTwoSelected(false);
					setExpThreeSelected(true);

					textMsgArea.append("Starting Experiment 3!!\n");
					main.threadProtocol = new Thread(main.networkProtocol, "Network_Protocol");
					main.threadProtocol.start();
				}

			}
		});

		GridBagConstraints gbc_btnStopComm = new GridBagConstraints();
		gbc_btnStopComm.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStopComm.insets = new Insets(0, 0, 5, 5);
		gbc_btnStopComm.gridx = 1;
		gbc_btnStopComm.gridy = 8;
		frame.getContentPane().add(btnStopComm, gbc_btnStopComm);
		btnStopComm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setStartComm(false);
			}
		});

		GridBagConstraints gbc_label_1BerValue = new GridBagConstraints();
		gbc_label_1BerValue.anchor = GridBagConstraints.WEST;
		gbc_label_1BerValue.insets = new Insets(0, 0, 5, 5);
		gbc_label_1BerValue.gridx = 3;
		gbc_label_1BerValue.gridy = 8;
		frame.getContentPane().add(lblBerValue, gbc_label_1BerValue);

		GridBagConstraints gbc_lblParsedData = new GridBagConstraints();
		gbc_lblParsedData.gridwidth = 3;
		gbc_lblParsedData.insets = new Insets(0, 0, 5, 5);
		gbc_lblParsedData.gridx = 0;
		gbc_lblParsedData.gridy = 9;
		frame.getContentPane().add(lblParsedData, gbc_lblParsedData);

		GridBagConstraints gbc_lblNetGraphRep = new GridBagConstraints();
		gbc_lblNetGraphRep.gridwidth = 4;
		gbc_lblNetGraphRep.insets = new Insets(0, 0, 5, 0);
		gbc_lblNetGraphRep.gridx = 3;
		gbc_lblNetGraphRep.gridy = 9;
		frame.getContentPane().add(lblNetGraphRep, gbc_lblNetGraphRep);

		GridBagConstraints gbc_scrollPaneParsed = new GridBagConstraints();
		gbc_scrollPaneParsed.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneParsed.gridwidth = 3;
		gbc_scrollPaneParsed.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPaneParsed.gridx = 0;
		gbc_scrollPaneParsed.gridy = 10;
		frame.getContentPane().add(scrollPaneParsed, gbc_scrollPaneParsed);
		scrollPaneParsed.setViewportView(textParsedArea);

		GridBagConstraints gbc_panelGraph = new GridBagConstraints();
		gbc_panelGraph.gridwidth = 4;
		gbc_panelGraph.fill = GridBagConstraints.BOTH;
		gbc_panelGraph.gridx = 3;
		gbc_panelGraph.gridy = 10;
		frame.getContentPane().add(animation, gbc_panelGraph);
		/*System.out.println(animation.getPreferredSize());
		animation.setSize(animation.getPreferredSize());
		panelGraph.add(animation);*/

		frame.setVisible(true);
	}

	public boolean isStartComm() {
		return startComm;
	}

	public void setStartComm(boolean startComm) {
		this.startComm = startComm;
	}

	public boolean isExpOneSelected() {
		return expOneSelected;
	}

	public void setExpOneSelected(boolean expOneSelected) {
		this.expOneSelected = expOneSelected;
	}

	public boolean isExpTwoSelected() {
		return expTwoSelected;
	}

	public void setExpTwoSelected(boolean expTwoSelected) {
		this.expTwoSelected = expTwoSelected;
	}

	public boolean isExpThreeSelected() {
		return expThreeSelected;
	}

	public void setExpThreeSelected(boolean expThreeSelected) {
		this.expThreeSelected = expThreeSelected;
	}

	@Override
	public void eventOccurred(EventClass e) {

		if (e == (EventClassListener) btnStopComm) {
			System.out.println("Stop communications");
		}
	}
}
