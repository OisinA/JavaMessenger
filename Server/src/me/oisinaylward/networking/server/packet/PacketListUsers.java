package me.oisinaylward.networking.server.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.oisinaylward.networking.server.Client;

public class PacketListUsers extends Packet {

	private ArrayList<Client> clients;
	
	public PacketListUsers(ArrayList<Client> clients) {
		this.clients = clients;
	}
	
	@Override
	public int getID() {
		return 2;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		stream.writeByte(getID());
		stream.writeByte(clients.size());
		for(Client user : clients) {
			stream.writeUTF(user.getUsername());
		}
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		//no reading
	}

}
