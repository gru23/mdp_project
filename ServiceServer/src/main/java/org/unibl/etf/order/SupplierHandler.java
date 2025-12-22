package org.unibl.etf.order;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.unibl.etf.order.enums.MessageType;
import org.unibl.etf.order.enums.OrderStatus;
import org.unibl.etf.parts.PartEntity;
import org.unibl.etf.parts.PartService;
import org.unibl.etf.suppliers.Supplier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SupplierHandler extends Thread {
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private Gson gson;
	
	private String supplierName;
	private OrderServer server;
	
	private Channel channel;
	
	public SupplierHandler(Socket socket, OrderServer server, Connection connection) {
		this.socket = socket;
		this.server = server;
		this.gson = new Gson();
		try {
			channel = connection.createChannel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			String line;
			
			while((line = in.readLine()) != null) {
				JsonParser parser = new JsonParser();
		        JsonObject root = parser.parse(line).getAsJsonObject();
		        MessageType type = MessageType.valueOf(root.get("type").getAsString());
		        
		        if(MessageType.ORDER_ACK == type) {
		        	MessageOrder message = gson.fromJson(line, MessageOrder.class);
					handleMessageOrder(message);
		        }
		        else {
		        	Message message = gson.fromJson(line, Message.class);
					handleMessage(message);
		        }
			}
		} catch(Exception e) {
//			e.printStackTrace();
			System.out.println("Dobavljač prekinuo vezu: " + supplierName);
			server.unregisterSupplier(supplierName);
		}
		finally {
	        try { if (channel != null) channel.close(); } catch (Exception ignored) {}
	    }
	}
	
	public void handleMessage(Message message) {
		switch (message.getType()) {
		case NEW_CUPCAKE:
			supplierName = message.getSupplierName();
			try {
				channel.queueDeclare(supplierName, false, false, false, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			server.registerSupplier(supplierName, this);
			server.addSupplier(new Supplier(supplierName, message.getPayload()));
			break;

		case ITEMS_UPDATE:
			System.out.println("Updated items by " + message.getSupplierName());
			//preuzeti azurirane podatke
			System.out.println("Broj artikala je " + message.getPayload().size());
			server.addSupplier(new Supplier(message.getSupplierName(), message.getPayload()));
			break;
			
		case ORDER_REQUEST:
		    System.out.println("Primljena narudžba od servisa za dobavljača " + message.getSupplierName());
		    // npr. obradi narudžbu i pošalji ACK
		    sendAck(MessageType.ORDER_ACK);
		    break;
		}
	}
	
	public void handleMessageOrder(MessageOrder msg) {
		System.out.println("Order processed by " + msg.getSupplierName());
		System.out.println("DODAJE SE OREDER SA ID " + msg.getPayload().getId());
		if(OrderStatus.APPROVED == msg.getPayload().getStatus()) {
			PartService partService = new PartService();
			ArrayList<PartEntity> parts = msg.getPayload()
					.getArticles()
					.stream()
					.map(PartEntity::new)
					.collect(Collectors.toCollection(ArrayList::new));
			for(PartEntity e : parts) {
				partService.add(e);
			}
		}
		OrderDAO.getInstance().addOrder(msg.getPayload());
	}
	
	public void send(Message message) {
		out.println(gson.toJson(message));
	}
	
	public void sendOrder(MessageOrder order) {		
		try {
			String json = gson.toJson(order);
			channel.basicPublish("", supplierName, null, json.getBytes("UTF-8"));
			//out.println(gson.toJson(order));
			System.out.println("Order proslijedjen supplier serveru (OrderClient)");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendAck(MessageType type) {
		Message ack = new Message();
		ack.setType(type);
		out.println(gson.toJson(ack));
	}
}
