package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gui.ChatPanel;
import utils.KryoSerialization;

public class GroupChatReceiver extends Thread {
	public static Set<String> onlineClients;

    private static final int PORT = 20000;
    private static final String HOST = "224.0.0.11";

    private ChatPanel chatPanel;
    

    public GroupChatReceiver(String name) {
    	onlineClients = new HashSet<String>();
    }

    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket(PORT)) {

            InetAddress group = InetAddress.getByName(HOST);
            socket.joinGroup(group);

            byte[] buffer = new byte[4096];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                byte[] data = Arrays.copyOf(packet.getData(), packet.getLength());
                
                String packetString = KryoSerialization.deserialize(data).toString();
                if(isCupcakePacket(packetString)) {
                	String newClientUsername = packetString.split("#")[1];
                	onlineClients.add(newClientUsername);
                	chatPanel.refreshUsersPanel();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean isCupcakePacket(String packetString) {
    	return packetString.startsWith("NEW_CUPCAKE#") || packetString.startsWith("OLD_CUPCAKE#");
    }
    
    public void setChatPanel(ChatPanel chatPanel) {
    	this.chatPanel = chatPanel;
    }
}

