package org.dongba.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.dongba.service.ServerChatService;

public class ChatServer {
	
	private static int uniqueId;
	
	private int port;
	
	private boolean keepGoing;
	
	private ServerChatService chatService;
	
	public ChatServer(int port) {
		this.port = port;
		this.chatService = new ServerChatService();
	}
	
	public void start() {
		keepGoing = true;
		
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(this.port);
		
			while(keepGoing) {
				display("Server waiting for clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();
				
				display("Client " + socket.getInetAddress().getHostName() + " is connected.");
				
				if (!keepGoing) {
					break;
				}
				
				ClientThread ct = new ClientThread(socket, this.chatService);
				
				this.chatService.add(ct);
				
				ct.start();
				
			}
		} catch (IOException e) {
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stop() {
		keepGoing = false;
	}
	
	public void display(String message) {
		this.chatService.display(message);
	}
	
	public static void main(String[] args) {
		int port = 1500;
		ChatServer server = new ChatServer(port);
		server.start();
	}

}
