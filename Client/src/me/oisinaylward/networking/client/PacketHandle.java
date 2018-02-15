package me.oisinaylward.networking.client;

import me.oisinaylward.networking.client.packet.Packet;
import me.oisinaylward.networking.client.packet.PacketHandler;
import me.oisinaylward.networking.client.packet.PacketListUsers;
import me.oisinaylward.networking.client.packet.PacketMessage;

public class PacketHandle implements PacketHandler {

	public PacketHandle() {
		Packet.addHandler(this);
	}
	
	@Override
	public void handle(Packet packet) {
		if(packet.getID() == 1) {
			PacketMessage message = (PacketMessage) packet;
			if(!message.getUsername().equals(""))
				Client.get().addMessage(message.getUsername() + " > " + message.getMessage());
			else
				Client.get().addMessage(message.getMessage());
		} else if(packet.getID() == 2) {
			PacketListUsers users = (PacketListUsers) packet;
			Client.get().getGUI().setUsers(users.getUsers());
		}
	}

}
