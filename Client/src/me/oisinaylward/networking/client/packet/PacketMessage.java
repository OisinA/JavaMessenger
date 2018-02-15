package me.oisinaylward.networking.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketMessage extends Packet {
	
	private String username;
	private String message;
	
	public PacketMessage(String username, String message) {
		this.message = message;
		this.username = username;
	}
	
	public PacketMessage(String message) {
		this.message = message;
		this.username = "";
	}
	
	public PacketMessage() {
		
	}
	
	@Override
	public int getID() {
		return 1;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getUsername() {
		return username;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		stream.writeByte(getID());
		stream.writeUTF(message);
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		String username = stream.readUTF();
		this.username = username.equals("0") ? "" : username;
		String message = stream.readUTF();
		this.message = message;
	}
	
}
