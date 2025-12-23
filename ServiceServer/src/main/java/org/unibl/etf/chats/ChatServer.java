package org.unibl.etf.chats;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import org.unibl.etf.util.Config;

public class ChatServer {
	private static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());
	
	private static final int PORT = Config.getInt("chat.unicast.port");
	private static final String KEY_STORE_PATH = Config.get("keystore.path");
	private static final String KEY_STORE_PASSWORD = Config.get("keystore.password");
	
	private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		LOGGER.info("Chat server started on port " + PORT);
		
		System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);

		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		
		try(ServerSocket serverSocket = ssf.createServerSocket(PORT)) {
			while(true) {
				SSLSocket clientSock = (SSLSocket) serverSocket.accept();
				LOGGER.info("New client connected: " + clientSock.getInetAddress());
	            new ClientHandler(clientSock);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "I/O Exception in Chat Server", e);
		}
	}
	
	public static synchronized void registerClient(String username, ClientHandler handler) {
		clients.put(username, handler);
		LOGGER.info("Registred: " + username);
	}
	
	public static synchronized void removeClient(String username) {
        clients.remove(username);
        LOGGER.info("Removed: " + username);
    }
	
	public static void forwardMessage(String sender, String receiver, String message) {
		ClientHandler handler = clients.get(receiver);
		
		if (handler != null) {
            handler.send(sender + "#" + receiver + "#" + message);
        } else {
            ClientHandler senderHandler = clients.get(sender);
            if (senderHandler != null) {
                senderHandler.send("SERVER#"+sender+"#User '" + receiver + "' is offline.");
            }
        }
	}
}
