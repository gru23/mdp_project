package org.unibl.etf.server.sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.unibl.etf.articles.Article;
import org.unibl.etf.server.articles.ArticleService;
import org.unibl.etf.server.articles.OrderingArticle;
import org.unibl.etf.utils.AppSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OrderClient {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Gson gson;
	
	private ArticleService articleService;
	
	public OrderClient(String supplierName) {
		try {
			this.socket = new Socket("localhost", 9000);
			this.gson = new Gson();
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			articleService = new ArticleService();
			sendIntroduction();
			new Thread(this::listen).start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendIntroduction() {
		ArrayList<OrderingArticle> orderings = convertArticleToOrderingArticle(articleService.getAllArticles());
		Message<OrderingArticle> message = new Message<OrderingArticle>(
					MessageType.NEW_CUPCAKE,  
					orderings
				);
		out.println(gson.toJson(message));
	}
	
    public void addNewItem() {
    	ArrayList<OrderingArticle> orderings = convertArticleToOrderingArticle(articleService.getAllArticles());
        Message<OrderingArticle> msg = new Message<OrderingArticle>(
	        		MessageType.ITEMS_UPDATE, 
	        		orderings
	        	);
        out.println(gson.toJson(msg));
    }
    
    public void updateOrder(String orderMessageJson) {
    	out.println(orderMessageJson);
    	System.out.println("Narudzba otisla iz OrderClient -> SupplierHandler");
    }

    private void listen() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
//            	Type messageType = new TypeToken<Message<OrderingArticle>>(){}.getType();
//                Message<OrderingArticle> msg = gson.fromJson(line, messageType);
//                handleMessage(msg);
            	
            	JsonParser parser = new JsonParser();
            	JsonObject root = parser.parse(line).getAsJsonObject();

                MessageType type =
                    MessageType.valueOf(root.get("type").getAsString());

                switch (type) {
                    case ORDER_REQUEST:
                        MessageOrder mo = gson.fromJson(root, MessageOrder.class);
                        handleMessageOrder(mo);
                        break;

//                    case ARTICLES:
                    default:
                        Message m = gson.fromJson(root, Message.class);
                        handleMessage(m);
                        break;
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Veza sa servisom prekinuta.");
        }
    }

    private void handleMessage(Message<OrderingArticle> msg) {
//        if (MessageType.ORDER_REQUEST == msg.getType()) {
//        	System.out.println(msg.getPayload().get(0) + " " + msg.getType());
//            System.out.println("Primljena narudžba");
//            sendOrderAck();
//        }
    }
    
    private void handleMessageOrder(MessageOrder msg) {
    	if(MessageType.ORDER_REQUEST == msg.getType()) {
    		System.out.println("Primljena narudžba, metoda handleMessageOrder");
//    		sendOrderAck(msg);
    		AppSession.getInstance().getSupplierServer().broadcastOrder(msg);
    		System.out.println("A OVO?!");
    	}
    }

    private void sendOrderAck(MessageOrder msg) {
        msg.setType(MessageType.ORDER_ACK);
    }
    
    private ArrayList<OrderingArticle> convertArticleToOrderingArticle(ArrayList<Article> articles) {
        return articles.stream()
                       .map(OrderingArticle::new)
                       .collect(Collectors.toCollection(ArrayList::new));
    }
}
