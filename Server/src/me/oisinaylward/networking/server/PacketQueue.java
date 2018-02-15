package me.oisinaylward.networking.server;

import java.util.ArrayList;

import me.oisinaylward.networking.server.packet.Packet;

public class PacketQueue {

	private ArrayList<Packet> queue;
	
	public PacketQueue() {
		queue = new ArrayList<Packet>();
	}
	
	public void add(Packet s) {
		queue.add(s);
	}
	
	public Packet get() {
		Packet s = queue.get(0);
		queue.remove(0);
		return s;
	}
	
	public Packet pop() {
		Packet s = queue.get(0);
		queue.remove(0);
		return s;
	}
	
	public void push(Packet s) {
		queue.add(s);
	}
	
	public int getSize() {
		int i = 0;
		ArrayList<Packet> replica = new ArrayList<Packet>(queue);
		for(Packet s : replica) {
			i++;
		}
		return i;
	}

}
