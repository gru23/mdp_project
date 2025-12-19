package org.unibl.etf;

import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import org.unibl.etf.client.gui.MainWindow;
import org.unibl.etf.client.sockets.SupplierClient;
import org.unibl.etf.server.sockets.SupplierServer;
import org.unibl.etf.utils.AppSession;

public class SupplierAppMain {

	public static void main(String[] args) {
		try {			
			SupplierServer server = new SupplierServer(0);
			server.start();
			AppSession.getInstance().setSupplierServer(server);
			
			int port = server.getPort();
			Socket clientSocket = new Socket("127.0.0.1", port);
			SupplierClient supplierClient = new SupplierClient(clientSocket);
//			new MainWindow(supplierClient);
			
			SwingUtilities.invokeLater(() -> {
                new MainWindow(supplierClient);
                supplierClient.startListening();
            });
			
			AppSession.getInstance().getOrderClient();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
