package org.unibl.etf.rmi.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.unibl.etf.rmi.model.Bill;
import org.unibl.etf.utils.AppSession;
import org.unibl.etf.utils.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BookkeepingServer implements BookkeepingInterface {
	private static final Logger LOGGER = Logger.getLogger(BookkeepingServer.class.getName());
	
	private static final String PATH = Config.get("path.resources");
	private static String supplierName;
	
	public ArrayList<Bill> bills;
	
		
	public BookkeepingServer() throws RemoteException {
		super();
		this.bills = new ArrayList<Bill>();
		supplierName = AppSession.getInstance().getSupplierName();
		writeRemoteReference();
	}
	
	public static void main(String[] args) {
		System.setProperty("java.security.policy", PATH + File.separator + Config.get("server.policyfile"));
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			BookkeepingServer server = new BookkeepingServer();
			BookkeepingInterface stub = (BookkeepingInterface) UnicastRemoteObject.exportObject(server,
					0);
			
			Registry registry;
			try {
			    registry = LocateRegistry.getRegistry(1099);
			    registry.list();
			} catch (Exception e) {
			    registry = LocateRegistry.createRegistry(1099);
			}
			
			registry.rebind("Bookkeeping_" + supplierName, stub);
			LOGGER.info("RMI server started.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void bookOrder(Bill bill) {
		bills.add(bill);
		LOGGER.info("Bill booked");
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
	        LOGGER.info("Bill saved: " + file.getAbsolutePath());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void writeRemoteReference() {
		try (FileWriter writer = new FileWriter(PATH + File.separator + Config.get("remote.reference"))) {
		    writer.write(supplierName);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
