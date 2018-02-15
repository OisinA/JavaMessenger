package me.oisinaylward.networking.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import me.oisinaylward.networking.server.packet.Packet;
import me.oisinaylward.networking.server.packet.PacketListUsers;

public class Client {
	
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	private PacketQueue packetQueue;
	
	private UUID uuid;
	
	private Thread readThread;
	private Thread writeThread;
	private boolean keepAlive = true;
	
	private String username;
	
	public Client(Socket socket) {
		this.socket = socket;
		uuid = UUID.randomUUID();
		packetQueue = new PacketQueue();
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public void disconnect() {
		keepAlive = false;
		try {
			socket.close();
			outputStream.close();
			inputStream.close();
		} catch(IOException e) {
			Log.error(e);
		}
		Log.info("connection", "User " + uuid.toString() + " disconnected.");
		Server.get().broadcast("User " + username + " has disconnected.");
		Server.get().removeUser(uuid);
		Server.get().broadcast(new PacketListUsers(new ArrayList<Client>(Server.get().getUsers())));
	}
	
	public void write(Packet packet) {
		packetQueue.add(packet);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void start() {
		try {
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			username = inputStream.readUTF();
		} catch(Exception e) {
			Log.error(e);
			return;
		}
		writeThread = new Thread() {
			public void run() {
				try {
					while(keepAlive) {
						int size = packetQueue.getSize();
						if(size > 0) {
							packetQueue.pop().write(outputStream);
						}
					}
				} catch(Exception e) {
					Log.error(e);
					disconnect();
					return;
				}
			};
		};
		readThread = new Thread() {
			public void run() {
				try {
					while(keepAlive) {
						Packet.readPacket(Client.this);
					}
				} catch(Exception e) {
					Log.error(e);
					disconnect();
					return;
				}
			};
		};
		writeThread.start();
		readThread.start();
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public DataInputStream getInputStream() {
		return inputStream;
	}
	
}
