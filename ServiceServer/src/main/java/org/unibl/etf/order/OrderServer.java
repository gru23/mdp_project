package org.unibl.etf.order;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import org.unibl.etf.suppliers.Supplier;
import org.unibl.etf.util.ConnectionFactoryUtil;

import com.rabbitmq.client.Connection;

public class OrderServer {
	public static final Map<String, Supplier> suppliers = new ConcurrentHashMap<>();

	
	private static final int PORT = 9000;
	
	private Map<String, SupplierHandler> suppliersHandler;
	
	private Connection connection;
	
	public OrderServer() {
		this.suppliersHandler = new ConcurrentHashMap<String, SupplierHandler>();
		try {
			this.connection = ConnectionFactoryUtil.createConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public void start() throws IOException {		
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Servis sluša na portu " + PORT);
			
			while(true) {
				Socket socket = serverSocket.accept();
				SupplierHandler handler = new SupplierHandler(socket, this, connection);
				handler.start();
			}
		}
	}
	
	public void registerSupplier(String supplierName, SupplierHandler handler) {
		suppliersHandler.put(supplierName, handler);
		System.out.println("Registred supplier " + supplierName);
	}
	
	public void unregisterSupplier(String supplierName) {
	    suppliersHandler.remove(supplierName);
	    suppliers.remove(supplierName);
	}
	
	public void sendOrder(String supplierName, MessageOrder order) {
		System.out.println("ORder stig'o do OrderServer tj. do servera servisera");
		SupplierHandler handler = suppliersHandler.get(supplierName);
		if(handler != null) {
			handler.sendOrder(order);
		}
		else
			System.out.println("Unknown supplier!");
	}
	
	public void addSupplier(Supplier supplier) {
		suppliers.put(supplier.getName(), supplier);
		System.out.println("ISPIS SVIH dobavlajca " + suppliers.values().size());
	}
	
	
}
