package org.unibl.etf.suppliers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceSocketServer {

    private int port;
    private ServerSocket serverSocket;
    private Map<String, SupplierHandlerThread> suppliers = new ConcurrentHashMap<>();

    public ServiceSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[SERVIS] Server pokrenut na portu: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVIS] Dobavljač se povezao...");

                SupplierHandlerThread handler = new SupplierHandlerThread(clientSocket, this);
                handler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerSupplier(String name, SupplierHandlerThread handler) {
        suppliers.put(name, handler);
        System.out.println("[SERVIS] Dobavljač registrovan: " + name);
    }

    public void unregisterSupplier(String name) {
        suppliers.remove(name);
        System.out.println("[SERVIS] Dobavljač isključen: " + name);
    }

    public Map<String, SupplierHandlerThread> getSuppliers() {
        return suppliers;
    }

    // Serviser ručno traži artikle
    public String requestItemsFrom(String supplierName) throws IOException {
        SupplierHandlerThread handler = suppliers.get(supplierName);
        if (handler == null) return null;

        handler.sendMessage("GET_ITEMS");

        return handler.waitForResponse(); // blokira dok ne primi ITEMS paket
    }

    // Serviser naručuje artikle
    public String orderPart(String supplierName, String code, int qty) throws IOException {
        SupplierHandlerThread handler = suppliers.get(supplierName);
        if (handler == null) return null;

        handler.sendMessage("ORDER|" + code + "|" + qty);

        return handler.waitForResponse(); // čeka odgovor ORDER_OK ili ORDER_ERROR
    }

    public static void main(String[] args) {
        ServiceSocketServer server = new ServiceSocketServer(5555);
        server.start();
    }
}
