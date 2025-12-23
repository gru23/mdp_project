package org.unibl.etf.suppliers;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.order.OrderingArticle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class SupplierHandlerThread extends Thread {
	private static final Logger LOGGER = Logger.getLogger(SupplierHandlerThread.class.getName());

    private Socket socket;
    private ServiceSocketServer server;

    private BufferedReader in;
    private PrintWriter out;

    private String supplierName = null;

    private String lastResponse = null;

    public SupplierHandlerThread(Socket socket, ServiceSocketServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Introducing message
            String helloMessage = in.readLine();
            if (helloMessage.startsWith("HELLO|")) {
                supplierName = helloMessage.split("\\|")[1];
                server.registerSupplier(supplierName, this);
                LOGGER.info("[SERVIS] Suppllier '" + supplierName + "' has been registered.");
                
                //moje za testiranje ispisa artikala dobavljaca - ovo ne treba biti tu
                out.println("GET_ITEMS");
                String primljeno = in.readLine();
                
                if (primljeno.startsWith("ITEMS|")) {
                    String json = primljeno.substring(6);
                    
                    Gson gson = new GsonBuilder().create();

                    Type listType = new TypeToken<ArrayList<OrderingArticle>>(){}.getType();
                    ArrayList<OrderingArticle> articles = gson.fromJson(json, listType);

                    for (OrderingArticle a : articles) {
                        System.out.println(a);
                    }
                }
                
            } else {
            	LOGGER.log(Level.SEVERE, "[SERVIS] Invalid HELLO package.");
                socket.close();
                return;
            }

            String line;
            while ((line = in.readLine()) != null) {
            	LOGGER.info("[SERVIS] Received from " + supplierName + ": " + line);

                synchronized (this) {
                    lastResponse = line;
                    this.notify(); 
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (supplierName != null) {
                server.unregisterSupplier(supplierName);
            }
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String waitForResponse() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return lastResponse;
        }
    }
}
