package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import gui.ChatPanel;
import models.ClientMessage;

public class UnicastChatReceiver extends Thread {
	private static final Logger LOGGER = Logger.getLogger(UnicastChatReceiver.class.getName());
	
	private Socket socket;
	
	private ChatPanel chatPanel;
	
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
            	chatPanel.receiveNewMessage(username, formattedMessage(new ClientMessage(username, split[2])), false);
            }
        } catch (Exception e) {
        	LOGGER.log(Level.SEVERE, "Disconnected from server.", e);
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
