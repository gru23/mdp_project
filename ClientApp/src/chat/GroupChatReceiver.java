package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.Set;

import models.dto.ClientMessage;
import utils.AppSession;
import utils.KryoSerialization;

public class GroupChatReceiver extends Thread {

    private static final int PORT = 20000;
    private static final String HOST = "224.0.0.11";

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
                	System.out.println("Novi kolacic " + packetString);
                	sendAcknowledgement();
                }
                else if(packetString.startsWith("OLD_CUPCAKE#")) {
                	String newClientUsername = packetString.split("#")[1];
                	if(newClientUsername.equals(name))
                		continue;
                	AppSession.getInstance().addOnlineClient(newClientUsername);
                	Set<String> sqw = AppSession.getInstance().getOnlineClients();
                	sqw.forEach(System.out::println);
                	System.out.println("Stari kolacic " + packetString);
                }
                else {
                	clientMessage = (ClientMessage) KryoSerialization.deserialize(data);
                    if(clientMessage.getUsername().equals(name))
                    	continue;
                    System.out.println(formattedMessage(clientMessage));
                    AppSession.getInstance()
                    	.getChatPanel()
                    	.receiveNewMessage(clientMessage.getUsername(), formattedMessage(clientMessage), true);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
    }
}

