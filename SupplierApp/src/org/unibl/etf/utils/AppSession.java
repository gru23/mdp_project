package org.unibl.etf.utils;

import java.util.Random;

import org.unibl.etf.server.sockets.OrderClient;
import org.unibl.etf.server.sockets.SupplierServer;

public class AppSession {

    private static AppSession instance;

    private String supplierName;
    
    private OrderClient orderClient;
    private SupplierServer supplierServer;
    

    private AppSession() {}

    public static synchronized AppSession getInstance() {
        if (instance == null) {
            instance = new AppSession();
        }
        return instance;
    }
    
    public String getSupplierName() {
    	if(supplierName == null)
    		supplierName = "Supplier " + new Random().nextInt(200);
    	return supplierName;
    }
    
    public OrderClient getOrderClient() {
    	if(orderClient == null)
    		orderClient = new OrderClient(getSupplierName());
    	return orderClient;
    }
    
    public void setSupplierServer(SupplierServer supplierServer) {
    	this.supplierServer = supplierServer;
    }
    
    public SupplierServer getSupplierServer() {
    	return supplierServer;
    }
}
