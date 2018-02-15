package me.oisinaylward.networking.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import me.oisinaylward.networking.client.Client;
import me.oisinaylward.networking.client.Log;

public class ClientGUI extends JFrame implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -150285510683686320L;
	
	private JTextField textField;
	private JButton sendButton;
	private JScrollPane pane;
	private JTextArea textArea;
	
	private JTextField usernameField;
	private JButton connectButton;
	
	private JTextArea connectUsers;
	
	private ArrayList<String> messages;
	private ArrayList<String> connected;

	public ClientGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			Log.error(e);
			return;
		}
		
		connected = new ArrayList<String>();
		
		messages = new ArrayList<String>();
		
		this.setTitle("Client");
		this.setSize(800, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		
		textField = new JTextField(10);
		textField.addKeyListener(this);
		textField.setFocusable(true);
		box.add(textField);
		
		sendButton = new JButton();
		sendButton.setText("Send");
		sendButton.addActionListener(this);
		box.add(sendButton);
		
		this.add(box, BorderLayout.SOUTH);
		
		Box box1 = Box.createHorizontalBox();
		box1.add(Box.createHorizontalGlue());
		
		box1.add(new JLabel("Username: "));
		usernameField = new JTextField();
		box1.add(usernameField);
		
		connectButton = new JButton();
		connectButton.setText("Connect");
		connectButton.addActionListener(this);
		box1.add(connectButton);
		
		this.add(box1, BorderLayout.NORTH);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText(messageString());
		pane = new JScrollPane(textArea);
		
		JSplitPane splitPane = new JSplitPane();
		connectUsers = new JTextArea();
		connectUsers.setEditable(false);
		JScrollPane pane1 = new JScrollPane(connectUsers);
		splitPane.setLeftComponent(pane);
		splitPane.setRightComponent(pane1);
		splitPane.setResizeWeight(0.8);
		splitPane.setDividerLocation(.8);
		this.add(splitPane, BorderLayout.CENTER);
	}
	
	private String messageString() {
		String message = "";
		for(String m : messages) {
			message = message + m + "\n";
		}
		return message;
	}
	
	public void addMessage(String message) {
		messages.add(message);
		textArea.setText(messageString());
		JScrollBar vertical = pane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public void addUser(String name) {
		this.connected.add(name);
		updateUsers();
	}
	
	private void updateUsers() {
		String s = "Connected:\n";
		for(String str : connected) {
			s = s + str + "\n";
		}
		this.connectUsers.setText(s);
	}
	
	public void setUsers(ArrayList<String> users) {
		this.connected = users;
		updateUsers();
	}
	
	public void removeUser(String name) {
		if(connected.contains(name)) {
			connected.remove(name);
		}
		updateUsers();
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if(action.getSource() == sendButton) {
			sendUserMessage();
		} else if(action.getSource() == connectButton) {
			String username = usernameField.getText();
			if(username.equals("")) {
				Log.info("connection", "Invalid username.");
				return;
			}
			Client.get().setUsername(usernameField.getText());
			Client.get().connect();
		}
	}

	@Override
	public void keyPressed(KeyEvent eve) {
		if(eve.getKeyCode() == KeyEvent.VK_ENTER) {
			sendUserMessage();
		}
	}
	
	private void sendUserMessage() {
		String message = textField.getText();
		if(message.equals(""))
			return;
		Client.get().sendMessage(message);
		textField.setText("");
	}

	@Override
	public void keyReleased(KeyEvent eve) {
	}

	@Override
	public void keyTyped(KeyEvent eve) {
		
	}
	
}
