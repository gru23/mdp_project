package org.unibl.etf.client.sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.unibl.etf.articles.Article;
import org.unibl.etf.client.gui.OrdersPanel;
import org.unibl.etf.orders.Order;
import org.unibl.etf.server.sockets.MessageOrder;
import org.unibl.etf.server.sockets.MessageType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class SupplierClient {
	
	private PrintWriter out;
	private BufferedReader in;
	
	private Gson gson;
	
	private OrdersPanel ordersPanel;

	
	public SupplierClient(Socket socket) {
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			gson = new Gson();
			
//			new Thread(this::listen).start();	//OVO NE OTVARA GUI!
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startListening() {
	    new Thread(this::listen, "SupplierClient-listener").start();
	}
	
	public ArrayList<Article> getAllArticles() {
	    ArrayList<Article> articles = new ArrayList<>();

	    try {
	        out.println("GET_ALL");
	        String line = in.readLine();

	        if (line != null && line.startsWith("OK")) {
	            Gson gson = new Gson();
	            String jsonString = line.split("\\|", 2)[1];

	            Type listType = new TypeToken<ArrayList<Article>>() {}.getType();
	            articles = gson.fromJson(jsonString, listType);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return articles;
	}

	public ArrayList<Article> addArticle(Article newArticle) {
		ArrayList<Article> articles = new ArrayList<>();
		Gson gson = new Gson();

	    try {
	    	String articleJSON = gson.toJson(newArticle);
	        out.println("ADD_ARTICLE|" + articleJSON);
	        String line = in.readLine();

	        if (line != null && line.startsWith("OK")) {
	            String jsonString = line.split("\\|", 2)[1];

	            Type listType = new TypeToken<ArrayList<Article>>() {}.getType();
	            articles = gson.fromJson(jsonString, listType);
	        }
	        else if (line.startsWith("ERROR")) {
	            System.err.println("Server error: " + line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return articles;
	}
	
	private void listen() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Supplier GUI primio: " + line);

                JsonParser parser = new JsonParser();
                JsonObject root = parser.parse(line).getAsJsonObject();

                MessageType type = MessageType.valueOf(root.get("type").getAsString());

                if (type == MessageType.ORDER_REQUEST) {
                    MessageOrder order = gson.fromJson(root, MessageOrder.class);
                    handleOrder(order);
                }
            }
        } catch (Exception e) {
            System.out.println("Veza prekinuta");
        }
    }

    private void handleOrder(MessageOrder order) {
        System.out.println("🔥 NOVA NARUDŽBA:");
        System.out.println(order);
        Order temp = order.getPayload();
        Order newOrder = new Order(temp.getId(), order.getSupplierName(), 
        		temp.getDate(), temp.getStatus(), temp.getArticles());
//        ordersPanel.addOrder(newOrder);
        System.out.println("UDJE LI?");
        SwingUtilities.invokeLater(() -> {
        	System.out.println("EVO OVDJE");
            ordersPanel.addOrder(newOrder);
        });
        // OVDJE:
        // - dodaj u GUI tabelu
        // - red čekanja
    }
    
    public void setOrdersPanel(OrdersPanel ordersPanel) {
    	this.ordersPanel = ordersPanel;
    }
}
