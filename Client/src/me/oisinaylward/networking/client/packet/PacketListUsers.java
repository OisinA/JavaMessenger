package me.oisinaylward.networking.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PacketListUsers extends Packet {

	private ArrayList<String> clients;
	
	public PacketListUsers(ArrayList<String> clients) {
		this.clients = clients;
	}
	
	public PacketListUsers() {
		
	}
	
	public ArrayList<String> getUsers() {
		return clients;
	}
	
	@Override
	public int getID() {
		return 2;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		int size = stream.readByte();
		clients = new ArrayList<String>();
		for(int i = 0; i < size; i++) {
			String username = stream.readUTF();
			clients.add(username);
		}
	}

}
