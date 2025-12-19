package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import gui.ChatPanel;
import models.ClientMessage;

public class UnicastChatReceiver extends Thread {
	private Socket socket;
	
	private ChatPanel chatPanel;
	
	public UnicastChatReceiver(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("UCR KRENO");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	System.out.println("UCR KRENO PETLJA");
            	System.out.println("ISpis iz UnicastReceiver-a: " + line);
            	String[] split = line.split("#");
            	String username = split[0];
            	System.out.println(username);
            	System.out.println(line);
            	chatPanel.receiveNewMessage(username, formattedMessage(new ClientMessage(username, split[2])), false);
            	System.out.println("UCR KRAJ PETLJE");
            }
        } catch (Exception e) {
            System.out.println("Disconnected from server.");
        }
	}
	
	private String formattedMessage(ClientMessage client) {
    	return "[" + client.getDateAndTime() + "] " 
    				+ client.getUsername() + ": "
    				+ client.getMessage();
    }
	
	public void setChatPanel(ChatPanel chatPanel) {
    	this.chatPanel = chatPanel;
    }
}
