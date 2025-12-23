package org.unibl.etf.server.sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.unibl.etf.articles.Article;
import org.unibl.etf.server.articles.ArticleService;
import org.unibl.etf.utils.AppSession;

import com.google.gson.Gson;

public class SupplierServer extends Thread {
	private static final Logger LOGGER = Logger.getLogger(SupplierServer.class.getName());
	
	private final ArticleService articleService;
	
	private ServerSocket serverSocket;
	
	private PrintWriter out;
	
	public SupplierServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.articleService = new ArticleService();
    }
	
	public int getPort() {
        return serverSocket.getLocalPort();
    }
	
	public void broadcastOrder(MessageOrder order) {
	    out.println("ORDER_REQUEST|" + new Gson().toJson(order));
	}
	
	@Override
	public void run() {
		try {
			Socket clientSocket = serverSocket.accept();
			LOGGER.info("Supplier client connected");
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
			
			String line;
			Gson gson = new Gson();
			LOGGER.info("Server started");
			while((line = in.readLine()) != null) {
				if(line.startsWith("ADD_ARTICLE")) {
					String[] split = line.split("\\|", 2);
					if (split.length < 2) {
                        out.println("ERROR|Invalid format");
                        continue;
                    }
					Article newArticle = gson.fromJson(split[1], Article.class);
					articleService.addArticle(newArticle);
					ArrayList<Article> updatedArticles = articleService.getAllArticles();
					//notifies service server about articles update
					AppSession.getInstance().getOrderClient().addNewItem();
					//answers to SupplierClient
					out.println("ADD_ARTICLE|" + gson.toJson(updatedArticles));
				}
				else if(line.startsWith("GET_ALL")) {
					out.println("GET_ALL|" + gson.toJson(articleService.getAllArticles()));
				}
				else if(line.startsWith("ORDER_UPDATE")) {
					String[] split = line.split("\\|", 2);
					if (split.length < 2) {
                        out.println("ERROR|Invalid format");
                        continue;
                    }
					AppSession.getInstance().getOrderClient().updateOrder(split[1]);
				}
				else 
					out.println("ERROR|Unknown command");
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
	}
}
