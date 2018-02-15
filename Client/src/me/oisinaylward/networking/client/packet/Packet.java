package me.oisinaylward.networking.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Packet {
	
	public static ArrayList<PacketHandler> handlers = new ArrayList<PacketHandler>();
	
	public Packet() {
		
	}
	
	public abstract int getID();
	
	public abstract void write(DataOutputStream stream) throws IOException;
	
	public abstract void read(DataInputStream stream) throws IOException;
	
	public static void readPacket(DataInputStream input) throws IOException {
		int id = input.readByte();
		Packet packet = idToPacket(id);
		if(packet == null)
			return;
		packet.read(input);
		handle(packet);
	}
	
	public static Packet idToPacket(int id) {
		switch(id) {
			case 0:
				return new PacketLogin();
			case 1:
				return new PacketMessage();
			case 2:
				return new PacketListUsers();
		}
		return null;
	}
	
	public static void addHandler(PacketHandler handler) {
		handlers.add(handler);
	}
	
	public static void handle(Packet packet) {
		for(PacketHandler pack : handlers) {
			pack.handle(packet);
		}
	}
	
}
