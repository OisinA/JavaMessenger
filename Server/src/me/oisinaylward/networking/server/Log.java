package me.oisinaylward.networking.server;

public class Log {
	
	public static void info(String tag, String info) {
		System.out.println("[" + tag.toUpperCase() + "] " + info);
	}
	
	public static void error(String info) {
		System.out.println("[ERROR] " + info);
	}
	
	public static void error(Exception e) {
		error(e.getMessage());
	}
	
}
