package me.oisinaylward.networking.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.oisinaylward.networking.client.gui.ClientGUI;
import me.oisinaylward.networking.client.packet.Packet;
import me.oisinaylward.networking.client.packet.PacketLogin;
import me.oisinaylward.networking.client.packet.PacketMessage;

public class Client {
	
	private Socket socket = null;
	
	private String ip;
	private int port;
	
	private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private boolean keepAlive = false;
	private DataOutputStream output;
	private DataInputStream input;
	
	private static Client client;
	
	private ClientGUI gui;
	
	private String username = "";
	
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
		gui = new ClientGUI();
		gui.setVisible(true);
		client = this;
		new PacketHandle();
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public static Client get() {
		return client;
	}
	
	public void sendMessage(String s) {
		if(socket == null) {
			Log.info("connection", "Not connected to a server.");
			return;
		}
		PacketMessage message = new PacketMessage(s);
		try {
			message.write(output);
		} catch(IOException e) {
			Log.error(e);
			return;
		}
		this.addMessage(username + " > " + s);
	}
	
	public void addMessage(String s) {
		gui.addMessage("[" + dateFormat.format(Calendar.getInstance().getTime()) + "] " + s);
	}
	
	public void connect() {
		if(username.equals("") || socket != null)
			return;
		try {
			socket = new Socket(ip, port);
			Log.info("connection", "Successfully opened socket.");
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			Log.info("connection", "Input/output streams created.");
			PacketLogin login = new PacketLogin(username);
			login.write(output);
			gui.addUser(username);
			Log.info("connection", "Sending details.");
			keepAlive = true;
			Log.info("connection", "Connection complete.");
			inputThread();
			return;
		} catch(IOException e) {
			stop();
			return;
		}
	}
	
	public ClientGUI getGUI() {
		return gui;
	}
	
	private void inputThread() {
		new Thread() {
			public void run() {
				while(keepAlive) {
					try {
						Packet.readPacket(input);
					} catch(IOException e) {
						Log.error(e);
						Client.this.stop();
						return;
					}
				}
				return;
			};
		}.start();
	}
	
	public void stop() {
		keepAlive = false;
		Log.info("connection", "Ending connection...");
	}
	
}
