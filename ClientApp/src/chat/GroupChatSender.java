package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import models.dto.ClientMessage;
import utils.KryoSerialization;

public class GroupChatSender {

    private static final int PORT = 20000;
    private static final String HOST = "224.0.0.11";

    private String name;

    public GroupChatSender(String name) {
        this.name = name;
        
        try (DatagramSocket socket = new DatagramSocket()) {
        	InetAddress group = InetAddress.getByName(HOST);
            byte[] newCupcake = KryoSerialization.serialize("NEW_CUPCAKE#" + name);
            DatagramPacket introductionPacket = new DatagramPacket(newCupcake, newCupcake.length, group, PORT);
            socket.send(introductionPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String messageToSend) {
    	try (DatagramSocket socket = new DatagramSocket()) {
    		ClientMessage clientMessage = new ClientMessage(name);
            InetAddress group = InetAddress.getByName(HOST);
            clientMessage.setMessage(messageToSend);
            
            byte[] buf = KryoSerialization.serialize(clientMessage);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, PORT);
            socket.send(packet);
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }
}
