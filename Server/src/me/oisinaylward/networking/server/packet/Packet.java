package me.oisinaylward.networking.server.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.oisinaylward.networking.server.Client;

public abstract class Packet {
	
	public static ArrayList<PacketHandler> handlers = new ArrayList<PacketHandler>();
	
	public Packet() {
		
	}
	
	public abstract int getID();
	
	public abstract void write(DataOutputStream stream) throws IOException;
	
	public abstract void read(DataInputStream stream) throws IOException;
	
	public static void readPacket(Client c) throws IOException {
		if(c == null || c.getInputStream() == null)
			return;
		int id = c.getInputStream().readByte();
		Packet packet = idToPacket(id);
		if(packet == null)
			return;
		packet.read(c.getInputStream());
		handle(c, packet);
	}
	
	public static Packet idToPacket(int id) {
		switch(id) {
			case 0:
				return new PacketLogin();
			case 1:
				return new PacketMessage();
		}
		return null;
	}
	
	public static void addHandler(PacketHandler handler) {
		handlers.add(handler);
	}
	
	public static void handle(Client c, Packet packet) {
		for(PacketHandler pack : handlers) {
			pack.handle(c, packet);
		}
	}
	
}
