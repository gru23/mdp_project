package servicerclientapp.chat;

import java.net.InetAddress;
import java.net.Socket;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class UnicastChat {

    public static final int TCP_PORT = 15000;
    private static final String SERVICER_CHAT_NAME = "MDP Servicer";

    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            Socket sock = new Socket(addr, TCP_PORT);

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
            out.println(SERVICER_CHAT_NAME);

            new UnicastChatReceiver(sock).start();
            new UnicastChatSender(sock, SERVICER_CHAT_NAME).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
