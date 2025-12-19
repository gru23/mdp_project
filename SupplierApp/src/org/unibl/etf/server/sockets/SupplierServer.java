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

import org.unibl.etf.articles.Article;
import org.unibl.etf.server.articles.ArticleService;
import org.unibl.etf.utils.AppSession;

import com.google.gson.Gson;

public class SupplierServer extends Thread {
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
	    Gson gson = new Gson();
	    out.println(gson.toJson(order));
	    System.out.println("A OVO 2?");
	}
	
	@Override
	public void run() {
		try {
			Socket clientSocket = serverSocket.accept();
			System.out.println("Supplier client connected");
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
			
			String line;
			Gson gson = new Gson();
			System.out.println("Server pokrenut i ceka");
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
					out.println("OK|" + gson.toJson(updatedArticles));
				}
				else if(line.startsWith("GET_ALL")) {
					out.println("OK|" + gson.toJson(articleService.getAllArticles()));
				}
				else 
					out.println("ERROR|Unknown command");
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
	}
}
