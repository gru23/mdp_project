package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.dto.ClientMessage;
import utils.AppSession;
import utils.Config;
import utils.KryoSerialization;

public class GroupChatReceiver extends Thread {
	private static final Logger LOGGER = Logger.getLogger(GroupChatSender.class.getName());

    private static final int PORT = Config.getInt("chat.group.port");
    private static final String HOST = Config.get("chat.group.host");

    private String name;
    
    

    public GroupChatReceiver(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket(PORT)) {

            InetAddress group = InetAddress.getByName(HOST);
            socket.joinGroup(group);

            byte[] buffer = new byte[4096];
            ClientMessage clientMessage;

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                
                String packetString = KryoSerialization.deserialize(data).toString();
                if(packetString.startsWith("NEW_CUPCAKE#")) {
                	LOGGER.info("New cupcake " + packetString);
                	sendAcknowledgement();
                }
                else if(packetString.startsWith("OLD_CUPCAKE#")) {
                	String newClientUsername = packetString.split("#")[1];
                	if(newClientUsername.equals(name))
                		continue;
                	AppSession.getInstance().addOnlineClient(newClientUsername);
                	LOGGER.info("Old cupcake " + packetString);
                }
                else {
                	clientMessage = (ClientMessage) KryoSerialization.deserialize(data);
                    if(clientMessage.getUsername().equals(name))
                    	continue;
                    AppSession.getInstance()
                    	.getChatPanel()
                    	.receiveNewMessage(clientMessage.getUsername(), formattedMessage(clientMessage), true);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "I/O Exception in group chat receiver for " + name);
        }
    }
    
    private String formattedMessage(ClientMessage client) {
    	return "[" + client.getDateAndTime() + "] " 
    				+ client.getUsername() + ": "
    				+ client.getMessage();
    }
    
    private void sendAcknowledgement() {
    	try (DatagramSocket socket = new DatagramSocket()) {
    		InetAddress group = InetAddress.getByName(HOST);
                
            byte[] newCupcake = KryoSerialization.serialize("OLD_CUPCAKE#" + name);
            DatagramPacket introductionPacket = new DatagramPacket(newCupcake, newCupcake.length, group, PORT);
            socket.send(introductionPacket);

            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "I/O Exception during acknowledging new client.");
            }
    }
}

