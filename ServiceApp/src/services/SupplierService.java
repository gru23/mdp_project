package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import exceptions.ServerError;
import models.Order;
import models.Supplier;
import utils.JSONConversion;
import utils.RestClient;

public class SupplierService {
	
	public ArrayList<Supplier> getAllSuppliers() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection("suppliers", false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			return JSONConversion.convertArrayList(jsonResponse, Supplier.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public ArrayList<Order> getAllOrders() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection("suppliers/orders", false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			return JSONConversion.convertArrayList(jsonResponse, Order.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public Order order(Order request) throws ServerError {
		HttpURLConnection conn = null;
		Order order = null;
		try {
			conn = RestClient.openConnection("suppliers/orders", true, "POST");
			RestClient.sendRequest(conn, request);
	        String response = RestClient.readResponse(conn);
	        order = RestClient.convertFromJSON(response, Order.class);
		} finally {
	        if (conn != null) conn.disconnect();
	    }
		return order;
	}
}
