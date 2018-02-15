package me.oisinaylward.networking.server;

import java.util.ArrayList;

import me.oisinaylward.networking.server.packet.Packet;
import me.oisinaylward.networking.server.packet.PacketHandler;
import me.oisinaylward.networking.server.packet.PacketListUsers;
import me.oisinaylward.networking.server.packet.PacketLogin;
import me.oisinaylward.networking.server.packet.PacketMessage;

public class PacketHandle implements PacketHandler {

	public PacketHandle() {
		Packet.addHandler(this);
	}
	
	@Override
	public void handle(Client c, Packet packet) {
		if(packet.getID() == 0) {
			PacketLogin login = (PacketLogin) packet;
			c.setUsername(login.getUsername());
			Server.get().broadcast(c.getUsername() + " has joined.");
			ArrayList<Client> clients = new ArrayList<Client>(Server.get().getUsers());
			System.out.println(clients.toString());
			Server.get().broadcast(new PacketListUsers(clients));
		} else if(packet.getID() == 1) {
			PacketMessage message = (PacketMessage) packet;
			Server.get().broadcast(c, message.getMessage());
		}
	}

}
