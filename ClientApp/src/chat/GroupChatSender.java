package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import models.dto.ClientMessage;
import utils.Config;
import utils.KryoSerialization;

public class GroupChatSender {
	private static final Logger LOGGER = Logger.getLogger(GroupChatSender.class.getName());

	private static final int PORT = Config.getInt("chat.group.port");
    private static final String HOST = Config.get("chat.group.host");

    private String name;

    public GroupChatSender(String name) {
        this.name = name;
        
        try (DatagramSocket socket = new DatagramSocket()) {
        	InetAddress group = InetAddress.getByName(HOST);
            byte[] newCupcake = KryoSerialization.serialize("NEW_CUPCAKE#" + name);
            DatagramPacket introductionPacket = new DatagramPacket(newCupcake, newCupcake.length, group, PORT);
            socket.send(introductionPacket);
            LOGGER.info(String.format("%s sent NEW_CUPCAKE package", name));
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
