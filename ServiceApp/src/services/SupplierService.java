package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.logging.Logger;

import chat.GroupChatReceiver;
import exceptions.ServerError;
import models.Order;
import models.Supplier;
import utils.Config;
import utils.JSONConversion;
import utils.RestClient;

public class SupplierService {
	private static final Logger LOGGER = Logger.getLogger(GroupChatReceiver.class.getName());
	
	public ArrayList<Supplier> getAllSuppliers() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.suppliers"), false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			LOGGER.info("Servicer fetching for all suppliers");
			return JSONConversion.convertArrayList(jsonResponse, Supplier.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public ArrayList<Order> getAllOrders() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.suppliers.orders"), false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			LOGGER.info("Servicer fetching for all orders");
			return JSONConversion.convertArrayList(jsonResponse, Order.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public Order order(Order request) throws ServerError {
		HttpURLConnection conn = null;
		Order order = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.suppliers.orders"), true, "POST");
			RestClient.sendRequest(conn, request);
	        String response = RestClient.readResponse(conn);
	        order = RestClient.convertFromJSON(response, Order.class);
	        LOGGER.info("Servicer sent order " + request.getId());
		} finally {
	        if (conn != null) conn.disconnect();
	    }
		return order;
	}
}
