package org.unibl.etf.suppliers;

import java.util.ArrayList;

import org.unibl.etf.order.OrderServer;

public class SupplierService {
	
	public SupplierService() {
	}
	
	public ArrayList<Supplier> getAll() {
		return new ArrayList<>(OrderServer.suppliers.values());
	}
}
