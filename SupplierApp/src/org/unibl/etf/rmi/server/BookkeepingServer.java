package org.unibl.etf.rmi.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.unibl.etf.rmi.model.Bill;
import org.unibl.etf.utils.AppSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BookkeepingServer implements BookkeepingInterface {
	
	private static final String PATH = "resources";
	private static String supplierName;
	
	public ArrayList<Bill> bills;
	
		
	public BookkeepingServer() throws RemoteException {
		super();
		this.bills = new ArrayList<Bill>();
		supplierName = AppSession.getInstance().getSupplierName();
		writeRemoteReference();
	}
	
	public static void main(String[] args) {
		System.setProperty("java.security.policy", PATH + File.separator + "server_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			BookkeepingServer server = new BookkeepingServer();
			BookkeepingInterface stub = (BookkeepingInterface) UnicastRemoteObject.exportObject(server,
					0);
			
			//Registry registry = LocateRegistry.createRegistry(1099);
			
			Registry registry;
			try {
			    registry = LocateRegistry.getRegistry(1099);
			    registry.list();
			} catch (Exception e) {
			    registry = LocateRegistry.createRegistry(1099);
			}
			
			registry.rebind("Bookkeeping_" + supplierName, stub);
			System.out.println("RMI server started.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void bookOrder(Bill bill) {
		bills.add(bill);
		System.out.println(bills.size());
		System.out.println("RACUN PROKNJIZEN!");
		saveBillToFile(bill);
	}

	private void saveBillToFile(Bill bill) {
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    String json = gson.toJson(bill);

	    File folder = new File("bills");
	    if (!folder.exists()) {
	        folder.mkdirs();
	    }

	    File file = new File(folder, bill.getOrder().getId() + ".json");

	    try (FileWriter writer = new FileWriter(file)) {
	        writer.write(json);
	        System.out.println("Bill saved: " + file.getAbsolutePath());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void writeRemoteReference() {
		try (FileWriter writer = new FileWriter(PATH + File.separator + "remote_reference.txt")) {
		    writer.write(supplierName);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
