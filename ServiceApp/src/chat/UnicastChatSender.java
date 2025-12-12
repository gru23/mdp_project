package chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UnicastChatSender extends Thread {
    private Socket socket;
    private String username;

    public UnicastChatSender(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            Scanner scan = new Scanner(System.in);

            System.out.println("Format for sending: receiver#message");
            System.out.println("Example: ana#cao, kako si?");
            System.out.println("Type 'exit' to quit");
            System.out.println("----------------------------------");

            while (true) {
                String input = scan.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                int idx = input.indexOf("#");
                if (idx == -1) {
                    System.out.println("Invalid format. Use: receiver#message");
                    continue;
                }

                String receiver = input.substring(0, idx);
                String message = input.substring(idx + 1);

                String formatted = username + "#" + receiver + "#" + message;
                out.println(formatted);
            }

            scan.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
