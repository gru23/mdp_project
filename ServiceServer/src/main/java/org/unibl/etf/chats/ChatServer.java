package org.unibl.etf.chats;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
	private static final int PORT = 15000;
	private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		System.out.println("Chat server started on port " + PORT);
		
		try(ServerSocket serverSocket = new ServerSocket(PORT)) {
			while(true) {
				Socket clientSock = serverSocket.accept();
				System.out.println("New client connected: " + clientSock.getInetAddress());
	            new ClientHandler(clientSock);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void registerClient(String username, ClientHandler handler) {
		clients.put(username, handler);
		System.out.println("Registered: " + username);
	}
	
	public static synchronized void removeClient(String username) {
        clients.remove(username);
        System.out.println("Removed: " + username);
    }
	
	public static void forwardMessage(String sender, String receiver, String message) {
		System.out.println("ISPIS Sa servera " + "  " + sender + " " + receiver + " " + message);
		System.out.print("ISPIS svih klijenata ");
		clients.keySet().forEach(System.out::println);
		ClientHandler handler = clients.get(receiver);
		
		if (handler != null) {
			System.out.println("ISPIS ako postoji handler: " + sender + "#" + receiver + "#" + message);
            handler.send(sender + "#" + receiver + "#" + message);
        } else {
            ClientHandler senderHandler = clients.get(sender);
            if (senderHandler != null) {
                senderHandler.send("SERVER#"+sender+"#User '" + receiver + "' is offline.");
            }
        }
	}
}
