package org.unibl.etf.suppliers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ServiceSocketServer {
	private static final Logger LOGGER = Logger.getLogger(ServiceSocketServer.class.getName());

    private int port;
    private ServerSocket serverSocket;
    private Map<String, SupplierHandlerThread> suppliers = new ConcurrentHashMap<>();

    public ServiceSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("[SERVIS] Service server started on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("[SERVIS] Supplier connected...");

                SupplierHandlerThread handler = new SupplierHandlerThread(clientSocket, this);
                handler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerSupplier(String name, SupplierHandlerThread handler) {
        suppliers.put(name, handler);
        LOGGER.info("[SERVIS] Supplier registred: " + name);
    }

    public void unregisterSupplier(String name) {
        suppliers.remove(name);
        LOGGER.info("[SERVIS] Supplier unregistred: " + name);
    }

    public Map<String, SupplierHandlerThread> getSuppliers() {
        return suppliers;
    }

    public String requestItemsFrom(String supplierName) throws IOException {
        SupplierHandlerThread handler = suppliers.get(supplierName);
        if (handler == null) return null;

        handler.sendMessage("GET_ITEMS");

        return handler.waitForResponse();
    }

    public String orderPart(String supplierName, String code, int qty) throws IOException {
        SupplierHandlerThread handler = suppliers.get(supplierName);
        if (handler == null) return null;

        handler.sendMessage("ORDER|" + code + "|" + qty);

        return handler.waitForResponse();
    }

    public static void main(String[] args) {
        ServiceSocketServer server = new ServiceSocketServer(5555);
        server.start();
    }
}
