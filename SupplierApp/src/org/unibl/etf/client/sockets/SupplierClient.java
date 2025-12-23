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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.unibl.etf.articles.Article;
import org.unibl.etf.client.gui.ArticlesPanel;
import org.unibl.etf.client.gui.OrdersPanel;
import org.unibl.etf.orders.Order;
import org.unibl.etf.server.sockets.MessageOrder;
import org.unibl.etf.server.sockets.MessageType;
import org.unibl.etf.utils.AppSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class SupplierClient {
	private static final Logger LOGGER = Logger.getLogger(SupplierClient.class.getName());
	
	private PrintWriter out;
	private BufferedReader in;
	
	private Gson gson;
	
	private OrdersPanel ordersPanel;
	private ArticlesPanel articlesPanel;

	
	public SupplierClient(Socket socket) {
		try {
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			gson = new Gson();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "I/O Exception in SupilerClient", e);
		}
	}
	
	public void startListening() {
	    new Thread(this::listen, "SupplierClient-listener").start();
	}
	
	public void getAllArticles() {
		out.println("GET_ALL");
	}

	public void addArticle(Article newArticle) {
		out.println("ADD_ARTICLE|" + gson.toJson(newArticle));
	}
	
	public void updateOrder(Order order) {
		MessageOrder messageOrder = new MessageOrder(
				MessageType.ORDER_ACK, 
				AppSession.getInstance().getSupplierName(), 
				order
		);
		out.println("ORDER_UPDATE|" + gson.toJson(messageOrder));
	}
	
	private void listen() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Supplier GUI primio: " + line);
                String[] parts = line.split("\\|", 2);
            	if (parts.length != 2) {
            	    continue;
            	}
                if(line.startsWith("GET_ALL") || line.startsWith("ADD_ARTICLE")) {
                	String json = parts[1];
                	Type listType = new TypeToken<ArrayList<Article>>() {}.getType();
                	ArrayList<Article> articles = gson.fromJson(json, listType);
                	articlesPanel.setAllArticles(articles);
                }
                else if(line.startsWith("ORDER_REQUEST")) {
	                JsonParser parser = new JsonParser();
	                JsonObject root = parser.parse(parts[1]).getAsJsonObject();
	
	                MessageType type = MessageType.valueOf(root.get("type").getAsString());
	
	                if (type == MessageType.ORDER_REQUEST) {
	                    MessageOrder order = gson.fromJson(root, MessageOrder.class);
	                    handleOrder(order);
	                }
                }
            }
        } catch (Exception e) {
        	LOGGER.log(Level.SEVERE, "Connection interrupted", e);
        }
    }

    private void handleOrder(MessageOrder order) {
    	LOGGER.info("New order income");
        Order temp = order.getPayload();
        Order newOrder = new Order(temp.getId(), order.getSupplierName(), 
        		temp.getDate(), temp.getStatus(), temp.getArticles());
        SwingUtilities.invokeLater(() -> {
            ordersPanel.addOrder(newOrder);
        });
    }
    
    public void setOrdersPanel(OrdersPanel ordersPanel) {
    	this.ordersPanel = ordersPanel;
    }
    
    public void setArticlesPanel(ArticlesPanel articlesPanel) {
    	this.articlesPanel = articlesPanel;
    }
}
