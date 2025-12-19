package chat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class UnicastChatSender {
    private String username;
    
    private PrintWriter out;

    public UnicastChatSender(Socket socket, String username) {
        this.username = username;
		try {
			System.out.println("UCS KRENO");
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			//out.println(username);
			System.out.println("posl'o username: " + username);
			System.out.println("UCS ZAVRSIO");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param message message to send with unicast
     * @param receiver username of user which will receive this message
     */
    public void send(String message, String receiver) {
    	System.out.println("UCS KRENO");
			String formattedMessage = username + "#" + receiver + "#" + message;
			out.println(formattedMessage);
			System.out.println("posl'o poruku: " + formattedMessage);
			System.out.println("UCS POSLO");
    }
}