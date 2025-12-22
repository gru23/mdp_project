package org.unibl.etf.rmi.client;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.unibl.etf.rmi.server.BookkeepingInterface;
import org.unibl.etf.utils.AppSession;

public class BookkeepingClient {

    private static final String PATH = "resources";
    private static BookkeepingInterface bookkeeper;

    static {
        System.setProperty("java.security.policy", PATH + File.separator + "client_policyfile.txt");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            String supplierName = AppSession.getInstance().getSupplierName();
            bookkeeper = (BookkeepingInterface) registry.lookup("Bookkeeping_" + supplierName);
            System.out.println("RMI Bookkeeping client connected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BookkeepingInterface getBookkeeper() {
        return bookkeeper;
    }
}
