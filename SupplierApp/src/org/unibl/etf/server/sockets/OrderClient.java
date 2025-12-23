package org.unibl.etf.server.sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.unibl.etf.articles.Article;
import org.unibl.etf.orders.OrderStatus;
import org.unibl.etf.rmi.model.Bill;
import org.unibl.etf.rmi.server.BookkeepingInterface;
import org.unibl.etf.server.articles.ArticleService;
import org.unibl.etf.server.articles.OrderingArticle;
import org.unibl.etf.utils.AppSession;
import org.unibl.etf.utils.Config;
import org.unibl.etf.utils.ConnectionFactoryUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class OrderClient {
	private static final Logger LOGGER = Logger.getLogger(OrderClient.class.getName());
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Gson gson;
	
	private ArticleService articleService;
	
	private Channel channel;
	private String supplierName;
	
	private BookkeepingInterface bookkeeper;
	
	public OrderClient(String supplierName) {
		try {
			this.socket = new Socket(Config.get("order.client.host"), Config.getInt("order.client.port"));
			this.gson = new Gson();
			this.supplierName = supplierName;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			articleService = new ArticleService();
			sendIntroduction();
			new Thread(this::listen).start();
			// MQ
			channel = ConnectionFactoryUtil.createConnection().createChannel();
			channel.queueDeclare(supplierName, false, false, false, null);
			receiveOrder();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
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
    
    public void receiveOrder() {
    	Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				
				String json = new String(body, "UTF-8");
				LOGGER.info("MQ primljena poruka: " + json);

	            MessageOrder messageOrder = gson.fromJson(json, MessageOrder.class);
				
				handleMessageOrder(messageOrder);
				LOGGER.info("Message received: '" + messageOrder + "'");
			}
		};
		try {
			channel.basicConsume(supplierName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void updateOrder(String orderMessageJson) {
    	sendBill(orderMessageJson);
    	out.println(orderMessageJson);
    }
    
    private void sendBill(String orderMessageJson) {
    	MessageOrder messageOrder = gson.fromJson(orderMessageJson, MessageOrder.class);
    	if(OrderStatus.APPROVED == messageOrder.getPayload().getStatus()) {
    		BigDecimal amountNoTax = messageOrder
    			    .getPayload()
    			    .getArticles()
    			    .stream()
    			    .map(a -> BigDecimal.valueOf(a.getPrice())
    			            .multiply(BigDecimal.valueOf(a.getQuanity())))
    			    .reduce(BigDecimal.ZERO, BigDecimal::add);
    		Bill bill = new Bill(supplierName, messageOrder.getPayload(), amountNoTax);
    		try {
				bookkeeper.bookOrder(bill);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
    	}
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
        	LOGGER.log(Level.SEVERE, "Veza sa servisom prekinuta.", e);
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
    		AppSession.getInstance().getSupplierServer().broadcastOrder(msg);
    	}
    }
    
    private ArrayList<OrderingArticle> convertArticleToOrderingArticle(ArrayList<Article> articles) {
        return articles.stream()
                       .map(OrderingArticle::new)
                       .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public void setBookkeeper(BookkeepingInterface bookkeeper) {
    	this.bookkeeper = bookkeeper;
    }
}
