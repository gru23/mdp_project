package org.unibl.etf.suppliers;

import java.util.ArrayList;

public class SupplierService {
	public ArrayList<Supplier> suppliers;	//ovo su mok podaci za sada, prave podatke treba dobaviti nekako sa dobavljacima...
	
	public SupplierService() {
		suppliers = new ArrayList<Supplier>();
		suppliers.add(new Supplier("Supplier 1"));
		suppliers.add(new Supplier("Supplier 2"));
		suppliers.add(new Supplier("Supplier 3"));
	}
	
	public ArrayList<Supplier> getAll() {
		return suppliers;
	}
}
