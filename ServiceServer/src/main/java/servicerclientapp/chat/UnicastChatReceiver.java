package servicerclientapp.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

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
            	System.out.println(formattedMessage(line));
            }
        } catch (Exception e) {
            System.out.println("Disconnected from server.");
        }
	}
	
	private String formattedMessage(String line) {
		String[] splitArray = line.split("#");
		int messageStartPosition = splitArray[0].length() + splitArray[1].length() + 2;
		String sender = splitArray[0];
		String message = line.substring(messageStartPosition);
		return sender + ": " + message;
	}
}
