package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.attribute.standard.NumberOfInterveningJobs;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame implements ActionListener {
	
	public static final int INVALID_VALUE = -1;

	private MonitorPanel monitorPanel;
	private JPanel panelTopControl;
	private JPanel panelBottomControl;
	private JButton buttonPop, buttonPush, buttonPause, buttonStep, buttonSkip;
	private JTextField textFieldInput;
	
	private long speed = 1000;

	public MainFrame() {
		super("Stack Linked list");
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		monitorPanel = new MonitorPanel(this);
		add(monitorPanel, BorderLayout.CENTER);
		setUpTopControl();
//		setUpBottomControl();
	}
	
	public long getSpeed() {
		return speed;
	}

	private void setUpTopControl() {
		panelTopControl = new JPanel();
		panelTopControl.setLayout(new FlowLayout());
		buttonPop = new JButton("Pop");
		buttonPop.addActionListener(this);
		buttonPush = new JButton("Push");
		buttonPush.addActionListener(this);
		textFieldInput = new JTextField(4);
		panelTopControl.add(textFieldInput);
		panelTopControl.add(buttonPush);
		panelTopControl.add(buttonPop);
		add(panelTopControl, BorderLayout.NORTH);
	}

	private void setUpBottomControl() {
		panelBottomControl = new JPanel();
		panelBottomControl.setLayout(new FlowLayout());
		buttonPause = new JButton("Pause");
		buttonStep = new JButton("Step");
		buttonSkip = new JButton("Skip");
		panelBottomControl.add(buttonPause);
		panelBottomControl.add(buttonStep);
		panelBottomControl.add(buttonSkip);
		add(panelBottomControl, BorderLayout.SOUTH);
	}

	public int getValueTextField() {
		if (textFieldInput.getText() == null || textFieldInput.getText().equals("")) {
			return INVALID_VALUE;
		}
		try {
			return Integer.parseInt(textFieldInput.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return INVALID_VALUE;
		}
	}

	public void visible() {
		setVisible(true);
	}
	
	public void setEnableTopControl(boolean enable) {
		if(!enable) {
			textFieldInput.setText("");
		}
		buttonPush.setEnabled(enable);
		buttonPop.setEnabled(enable);
	}

	public static void main(String args[]) {
		MainFrame mainFrame = new MainFrame();
		mainFrame.visible();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == buttonPush) {
			monitorPanel.doPush();
		} else if(e.getSource() == buttonPop) {
			monitorPanel.doPop();
		}
	}

}
