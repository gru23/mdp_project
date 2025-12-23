package chat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnicastChatSender {
	private static final Logger LOGGER = Logger.getLogger(UnicastChatSender.class.getName());
	
    private String username;
    
    private PrintWriter out;

    public UnicastChatSender(Socket socket, String username) {
        this.username = username;
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			LOGGER.info("Servicer sent handshake.");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "I/O Exception during instancing unicast chat for " + username, e);
		}
    }
    
    /**
     * 
     * @param message message to send with unicast
     * @param receiver username of user which will receive this message
     */
    public void send(String message, String receiver) {
		String formattedMessage = username + "#" + receiver + "#" + message;
		out.println(formattedMessage);
		LOGGER.info("Servicer sent message.");
    }
}