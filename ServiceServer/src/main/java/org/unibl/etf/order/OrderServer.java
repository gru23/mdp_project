package org.unibl.etf.order;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.unibl.etf.suppliers.Supplier;

public class OrderServer {
	public static final Map<String, Supplier> suppliers = new ConcurrentHashMap<>();

	
	private static final int PORT = 9000;
	
	private Map<String, SupplierHandler> suppliersHandler;
	
	public OrderServer() {
		suppliersHandler = new ConcurrentHashMap<String, SupplierHandler>();
	}

	public void start() throws IOException {		
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Servis sluša na portu " + PORT);
			
			while(true) {
				Socket socket = serverSocket.accept();
				SupplierHandler handler = new SupplierHandler(socket, this);
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
