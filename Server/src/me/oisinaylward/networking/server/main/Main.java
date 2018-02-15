package me.oisinaylward.networking.server.main;

import me.oisinaylward.networking.server.Server;

public class Main {
	
	public static void main(String[] args) {
		Server server = new Server(1337);
		server.start();
	}
	
}
