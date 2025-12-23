package org.unibl.etf.chats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	
	private Socket sock;
	private BufferedReader in;
	private PrintWriter out;
	private String username;
	
	public ClientHandler(Socket sock) {
		this.sock = sock;
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "Error while making client handler", e);
		}
		start();
	}
	
	@Override
	public void run() {
		try {
			username = in.readLine();
            ChatServer.registerClient(username, this);

            String line;
            while ((line = in.readLine()) != null) {

                // Format: sender#receiver#message
                String[] parts = line.split("#", 3);
                if (parts.length != 3) {
                	LOGGER.log(Level.WARNING, "Invalid message format from " + username + ": " + line);
                    continue;
                }

                String sender = parts[0];
                String receiver = parts[1];
                String message = parts[2];

                ChatServer.forwardMessage(sender, receiver, message);
            }

        } catch (Exception e) {
        	LOGGER.log(Level.SEVERE, "Client disconnected: " + username, e);
        }
        finally {
            ChatServer.removeClient(username);
            try { sock.close(); } catch (Exception e) {}
        }
    }

    public void send(String msg) {
        out.println(msg);
    }
}
