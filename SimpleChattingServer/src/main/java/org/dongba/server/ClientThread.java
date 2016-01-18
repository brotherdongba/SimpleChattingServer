package org.dongba.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;
import org.dongba.model.ChatMessage;
import org.dongba.service.ServerChatService;

public class ClientThread extends Thread {
	
	private String name;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private ChatMessage cm;
	private ServerChatService chatService;

	public ClientThread(Socket socket, ServerChatService chatService) throws IOException {
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		this.outputStream.flush();
		this.inputStream = new ObjectInputStream(socket.getInputStream());
		this.name = socket.getInetAddress().getHostName();
		this.chatService = chatService;
	}

	public void run() {
		while (true) {
			try {
				this.cm = (ChatMessage) this.inputStream.readObject();
			} catch (ClassNotFoundException e) {
				break;
			} catch (IOException e) {
				break;
			}
			
			String message = "";
			if (this.cm != null) {
				message = this.cm.getMessage();
			}
			
			if (!StringUtils.isBlank(message)) {
				chatService.broadcast(message);
			}
		}
	}

	public boolean writeMsg(String messageLf) {
		try {
			this.outputStream.writeObject(messageLf);
			this.outputStream.flush();
		} catch (IOException e) {
			return false;
		} finally {
			return true;
		}
	}

}
