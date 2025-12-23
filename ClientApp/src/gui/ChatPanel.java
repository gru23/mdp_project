package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import chat.GroupChatSender;
import chat.UnicastChatSender;
import models.dto.ClientMessage;
import utils.AppSession;
import utils.Config;

public class ChatPanel extends JPanel {
	private static final long serialVersionUID = -3594048662277078127L;
	
	private GroupChatSender groupSender;
	private UnicastChatSender unicastSender;
	// three panels that forms ChatPanel as a component
	private JPanel usersPanel;
	private JPanel messagesPanel; 
	private JPanel textingPanel;
	
	private JTextArea messagesArea;
	
	/**
	 * labels that represents online users and placed in userPanel
	 */
	private LinkedList<JLabel> usersLabel;	
	/**
	 * represents selected user which means that chat his chat is opened  
	 */
	private JLabel selectedUser;	
	
	private Map<String, String> messagesHistory;
	
	public ChatPanel() {
		super(new BorderLayout());
		messagesHistory = new HashMap<>();
		
		add(initializeUsersPanel(), BorderLayout.WEST);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(initializeMessagesPanel(), BorderLayout.CENTER);
		rightPanel.add(initializeTextAndSendPanel(), BorderLayout.SOUTH);
		
		add(rightPanel, BorderLayout.CENTER);
		
		AppSession.getInstance().setChat(this);
		groupSender = AppSession.getInstance().getGroupChatSender();
		unicastSender = AppSession.getInstance().getUnicastChatSender();
	}
	
	/**
	 * 
	 * @param username sender's username
	 * @param formattedMessage received formatted message '[date time] username: message' 
	 * @param isMulticast true - message destination is 'All users' chat. false - message destination is private chat
	 */
	public void receiveNewMessage(String username, String formattedMessage, boolean isMulticast) {
		String destinationChat = isMulticast ? "All users" : username;
		StringBuilder chat = new StringBuilder(messagesHistory.get(destinationChat));
		if(chat.length() > 0)
			chat.append("\n");
		chat.append(formattedMessage);
		messagesHistory.put(destinationChat, chat.toString());
		if(getSelectedUsername() != null && destinationChat.equals(getSelectedUsername())) {
			refreshMessagesScreen();
		}
	}
	
	public void refreshUsersPanel() {
        JPanel newUsersPanel = initializeUsersPanel();
        
        remove(usersPanel);
        usersPanel = newUsersPanel;
        add(usersPanel, BorderLayout.WEST);
        
        usersPanel.revalidate();
        usersPanel.repaint();
    }
	
	private JPanel initializeUsersPanel() {
		usersPanel = new JPanel();
		usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
		usersPanel.setBorder(new EmptyBorder(15, 10, 10, 10));
		usersPanel.setBackground(Color.WHITE);
		String[] users = AppSession.getInstance().getOnlineClients()
		        .stream()
		        .toArray(String[]::new);
		usersLabel = new LinkedList<>();
		for(String username : users) {
			if(!messagesHistory.containsKey(username))
				messagesHistory.put(username, "");
			JLabel usernameLb = new JLabel(username);
			usersLabel.add(usernameLb);
			if(Config.get("servicer.name").equals(username))
				messagesHistory.put(username, Config.get("servicer.name") + ": We are at your service :)");
			if("All users".equals(username)) {
				usernameLb.setForeground(Color.BLUE);
				usernameLb.setText("<html><u>" + username + "<u><html>");
				selectedUser = usernameLb;
			}
			usernameLb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			usernameLb.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	if(selectedUser != null)
	            		unselectCurrentUser();
	            	selectedUser = usernameLb; 
	            	usernameLb.setForeground(Color.BLUE);
	            	usernameLb.setText("<html><u>" + username + "<u><html>");
	            	messagesArea.setText(messagesHistory.get(username));
	            }
	        });
			usersPanel.add(Box.createVerticalStrut(5));
			usersPanel.add(usernameLb);
		}
		return usersPanel;		
	}
	
	private void unselectCurrentUser() {
		String stripHTML = selectedUser.getText().replace("<html>", "");
		stripHTML = stripHTML.replace("<u>", "");
		selectedUser.setText(stripHTML);
		selectedUser.setForeground(Color.BLACK);
	}
	
	private String getSelectedUsername() {
		String stripHTML = selectedUser.getText().replace("<html>", "");
		stripHTML = stripHTML.replace("<u>", "");
		return stripHTML.strip();
	}
	
	private JPanel initializeMessagesPanel() {
		messagesPanel = new JPanel();
		messagesPanel.setLayout(new BorderLayout());
		messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(messagesArea);
        messagesPanel.add(scrollPane, BorderLayout.CENTER);
		return messagesPanel;
	}
	
	private JPanel initializeTextAndSendPanel() {
	    textingPanel = new JPanel();
	    textingPanel.setLayout(new BorderLayout(10, 10));
	    textingPanel.setBackground(Color.WHITE);

	    JTextArea textArea = new JTextArea(4, 30);
        textArea.setLineWrap(true);
        textArea.setBorder(new LineBorder(Color.GRAY, 1, true));
        
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	    buttonPanel.setBackground(Color.WHITE);
	    JButton sendButton = new JButton("Send");
	    sendButton.addActionListener(e -> {
	    	String message = textArea.getText();
	    	String username = AppSession.getInstance().getCurrentClient().getUsername();
	    	appendMessageToChatHistory(formattedMessage(new ClientMessage(username, message)));
	    	if("All users".equals(getSelectedUsername()))
	    		groupSender.send(message);
	    	else 
	    		unicastSender.send(message, getSelectedUsername());
	    	textArea.setText("");
	    	refreshMessagesScreen();
	    });
	    buttonPanel.add(sendButton);
	    
	    textingPanel.add(textArea, BorderLayout.CENTER);
	    textingPanel.add(buttonPanel, BorderLayout.EAST);
	    return textingPanel;
	}
	
	private void appendMessageToChatHistory(String message) {
		String username = getSelectedUsername();
		StringBuilder chat = new StringBuilder(messagesHistory.get(username));
		if(chat.length() > 0)
			chat.append("\n");
		chat.append(message);
		messagesHistory.put(username, chat.toString());
	}
	
	private void refreshMessagesScreen() {
		String history = messagesHistory.get(getSelectedUsername());
		messagesArea.setText(history);
	    messagesArea.revalidate();
	    messagesArea.repaint();
	}
	
	private String formattedMessage(ClientMessage client) {
    	return "[" + client.getDateAndTime() + "] " 
    				+ client.getUsername() + ": "
    				+ client.getMessage();
    }
}
