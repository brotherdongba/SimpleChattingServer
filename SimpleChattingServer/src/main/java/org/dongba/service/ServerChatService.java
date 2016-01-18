package org.dongba.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.dongba.server.ClientThread;

public class ServerChatService {
	
	private ArrayList<ClientThread> clients;
	
	private SimpleDateFormat sdf;
	
	public ServerChatService() {
		this.sdf = new SimpleDateFormat("HH:mm:ss");
		this.clients = new ArrayList<ClientThread>();
	}
	
	public void display(String message) {
		String time = sdf.format(new Date(System.currentTimeMillis()));
		String messageLf = time + " " + message + "\n";
		System.out.println(messageLf);
	}

	public void broadcast(String message) {
		String time = sdf.format(new Date(System.currentTimeMillis()));
		String messageLf = time + " " + message + "\n";
		
		for (int index = clients.size() - 1 ; index >= 0 ; --index) {
			ClientThread ct = clients.get(index);
			
			System.out.println("broadcast message : " + message);
			if (!ct.writeMsg(messageLf)) {
				clients.remove(index);
				display("Distennected Client " + ct.getName() + " removed from list.");
			}
		}
	}

	public void add(ClientThread ct) {
		this.clients.add(ct);
	}

}
