package me.oisinaylward.networking.server.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketLogin extends Packet {

	private String username;
	
	public PacketLogin(String username) {
		this.username = username;
	}
	
	public PacketLogin() {
		
	}
	
	@Override
	public int getID() {
		return 0;
	}
	
	public String getUsername() {
		return username;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		stream.writeByte(getID());
		stream.writeUTF(username);
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		stream.read();
		int length = stream.read();
		byte[] username = new byte[length];
		for(int i = 0; i < length; i++) {
			username[i] = stream.readByte();
		}
		this.username = new String(username);
	}

}
