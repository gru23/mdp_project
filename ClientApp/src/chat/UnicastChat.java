package chat;

import java.net.InetAddress;
import java.net.Socket;

import utils.AppSession;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class UnicastChat {

    public static final int TCP_PORT = 15000;

    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            Socket sock = new Socket(addr, TCP_PORT);

            String username = AppSession.getInstance().getCurrentClient().getUsername();

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
            out.println(username);

            new UnicastChatReceiver(sock).start();
//            new UnicastChatSender(sock, username).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
