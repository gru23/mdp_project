package utils;

import java.util.HashSet;
import java.util.Set;

import chat.GroupChatSender;
import chat.UnicastChatSender;
import gui.ChatPanel;
import models.Client;

public class AppSession {

    private static AppSession instance;

    private Client currentClient;
    private Set<String> onlineClients;
    
    private ChatPanel chat;
    
    /**
     * used from ChatPanel class for sending multicast messages
     */
    private GroupChatSender groupChatSender;
    
    private UnicastChatSender unicastChatSender;
    

    private AppSession() {}

    public static synchronized AppSession getInstance() {
        if (instance == null) {
            instance = new AppSession();
        }
        return instance;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }
    
    public ChatPanel getChatPanel() {
    	return chat;
    }
    
    public void setChat(ChatPanel chat) {
    	this.chat = chat;
    }
    
    public void addOnlineClient(String username) {
    	if(onlineClients == null) {
    		onlineClients = new HashSet<>();
    		onlineClients.add("MDP Servicer");
    		onlineClients.add("All users");
    	}
    	onlineClients.add(username);
    	if(chat != null)
    		chat.refreshUsersPanel();
    }
    
    public Set<String> getOnlineClients() {
    	if(onlineClients == null) {
    		onlineClients = new HashSet<>();
    		onlineClients.add("MDP Servicer");
    		onlineClients.add("All users");
    	}
    	return onlineClients;
    }
    
    public void setGroupChatSender(GroupChatSender gcs) {
    	this.groupChatSender = gcs;
    }
    
    public GroupChatSender getGroupChatSender() {
    	return groupChatSender;
    }
    
    public void setUnicastChatSender(UnicastChatSender unicastChatSender) {
    	this.unicastChatSender = unicastChatSender;
    }
    
    public UnicastChatSender getUnicastChatSender() {
    	return unicastChatSender;
    }
    
    public void logout() {
    	//tu implementirati da se prilikom odjave posalje mnulticast svima kako bi oni uklonili korisnika iz chat-a
//    	groupChatSender.send(null);
        currentClient = null;
        onlineClients = null;
    }
}
