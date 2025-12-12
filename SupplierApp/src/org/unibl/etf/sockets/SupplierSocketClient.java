package org.unibl.etf.sockets;

import java.io.*;
import java.net.Socket;

import org.unibl.etf.articles.ArticleService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SupplierSocketClient {
	private final ArticleService service;

    private String name;
    private String host;
    private int port;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SupplierSocketClient(String name, String host, int port) {
        this.service = new ArticleService();
		this.name = name;
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(host, port);
            System.out.println("[DOBAVLJAC] Povezan na servis.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Introducing
            out.println("HELLO|" + name);

            // Čekaj komande
            String cmd;
            while ((cmd = in.readLine()) != null) {
                System.out.println("[DOBAVLJAC] Primljena komanda: " + cmd);

                if (cmd.equals("GET_ITEMS")) {
                    out.println("ITEMS|" + getItemsJson());
                } 
                else if (cmd.startsWith("ORDER|")) {
                    String[] parts = cmd.split("\\|");
                    String code = parts[1];
                    int qty = Integer.parseInt(parts[2]);

                    boolean ok = hasPart(code);

                    if (ok) {
                        double price = calculatePrice(code, qty);
                        out.println("ORDER_OK|" + code + "|" + qty + "|" + price);
                    } else {
                        out.println("ORDER_ERROR|" + code + "|Nema na stanju");
                    }

                } else {
                    System.out.println("[DOBAVLJAC] Nepoznata komanda.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasPart(String code) {
        return code.equals("BR123"); // test primjer
    }

    private double calculatePrice(String code, int qty) {
        double base = 100.0; // test cijena
        double pdv = base * 0.17;
        return (base + pdv) * qty;
    }

    private String getItemsJson() {
    	Gson gson = new GsonBuilder().create();
        return gson.toJson(service.getAllArticles());
    }

    public static void main(String[] args) {
        SupplierSocketClient client = new SupplierSocketClient("DobavljacA", "localhost", 5555);
        client.start();
    }
}
