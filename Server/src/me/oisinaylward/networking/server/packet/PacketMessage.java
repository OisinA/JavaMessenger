package me.oisinaylward.networking.server.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.oisinaylward.networking.server.Client;

public class PacketMessage extends Packet {

	private Client c;
	private String message;
	
	public PacketMessage(Client c, String message) {
		this.message = message;
		this.c = c;
	}
	
	public PacketMessage(String message) {
		this.message = message;
		this.c = null;
	}
	
	public PacketMessage() {
		
	}
	
	@Override
	public int getID() {
		return 1;
	}
	
	public void setClient(Client c) {
		this.c = c;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		stream.writeByte(getID());
		stream.writeUTF(c == null ? "0" : c.getUsername());
		stream.writeUTF(message);
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		String message = stream.readUTF();
		this.message = message;
	}

}
