package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import gui.ChatPanel;
import utils.Config;
import utils.KryoSerialization;

public class GroupChatReceiver extends Thread {
	private static final Logger LOGGER = Logger.getLogger(GroupChatReceiver.class.getName());
	public static Set<String> onlineClients;

    private static final int PORT = Config.getInt("chat.group.port");
    private static final String HOST = Config.get("chat.group.host");

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
            LOGGER.log(Level.SEVERE, "I/O Exception in group chat", e);
        }
    }
    
    private boolean isCupcakePacket(String packetString) {
    	return packetString.startsWith("NEW_CUPCAKE#") || packetString.startsWith("OLD_CUPCAKE#");
    }
    
    public void setChatPanel(ChatPanel chatPanel) {
    	this.chatPanel = chatPanel;
    }
}

