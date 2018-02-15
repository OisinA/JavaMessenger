package me.oisinaylward.networking.server.packet;

import me.oisinaylward.networking.server.Client;

public interface PacketHandler {
	
	public void handle(Client c, Packet packet);
	
}
