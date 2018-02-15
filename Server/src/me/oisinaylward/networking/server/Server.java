package me.oisinaylward.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import me.oisinaylward.networking.server.packet.Packet;
import me.oisinaylward.networking.server.packet.PacketMessage;

public class Server {
	
	private int port;
	
	private ServerSocket serverSocket;
	private boolean keepAlive = false;
	
	private static Server server;
	
	private HashMap<UUID, Client> connectedClients = new HashMap<UUID, Client>();
	
	public Server(int port) {
		this.port = port;
		server = this;
		new PacketHandle();
	}
	
	public void shutdown() {
		Log.info("shutdown", "Shutting down server.");
		if(serverSocket != null) {
			try {
				serverSocket.close();
				Log.info("shutdown", "Server socket successfully shut down.");
			} catch(IOException e) {
				Log.error("Error shutting down server socket.");
			}
		}
		Log.info("shutdown", "Shutdown complete.");
	}
	
	private void updateLoop() {
		try {
			Log.info("connection", "Awaiting connection request.");
			Socket socket = serverSocket.accept();
			Log.info("connection", "Connection request from " + socket.getInetAddress().getHostAddress() + ".");
			Client client = new Client(socket);
			Log.info("connection", "Accepted connection, placing client into list.");
			client.start();
			if(connectedClients.containsKey(client.getUUID()))
				return;
			connectedClients.put(client.getUUID(), client);
		} catch(IOException e) {
			Log.error(e);
		}
	}
	
	public void broadcast(Client client, String message) {
		for(Client c : connectedClients.values()) {
			if(c.getUUID() != client.getUUID())
				c.write(new PacketMessage(client, message));
		}
		Log.info(client.getUsername(), message);
	}
	
	public void broadcast(Packet packet) {
		for(Client c : connectedClients.values()) {
			c.write(packet);
		}
	}
	
	public void broadcast(String message) {
		for(Client c : connectedClients.values()) {
				c.write(new PacketMessage(message));
		}
		Log.info("broadcast", message);
	}
	
	public Collection<Client> getUsers() {
		return connectedClients.values();
	}
	
	public void removeUser(UUID uuid) {
		connectedClients.remove(uuid);
	}
	
	public static Server get() {
		return server;
	}
	
	public void start() {
		try {
			Log.info("startup", "Starting up server socket on port " + port + ".");
			serverSocket = new ServerSocket(port);
			keepAlive = true;
			Log.info("startup", "Starting client loop.");
			while(keepAlive) {
				updateLoop();
			}
		} catch(IOException e) {
			Log.error(e);
			shutdown();
		}
	}
	
}
