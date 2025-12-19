package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import models.dto.ClientMessage;
import utils.AppSession;

public class UnicastChatReceiver extends Thread {
	private Socket socket;
	
	public UnicastChatReceiver(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	String[] split = line.split("#");
            	String username = split[0];
            	System.out.println(username);
            	System.out.println(line);
            	AppSession.getInstance()
            	.getChatPanel()
            	.receiveNewMessage(username, formattedMessage(new ClientMessage(username, split[2])), false);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Disconnected from server.");
        }
	}
	
	private String formattedMessage(ClientMessage client) {
    	return "[" + client.getDateAndTime() + "] " 
    				+ client.getUsername() + ": "
    				+ client.getMessage();
    }
}
