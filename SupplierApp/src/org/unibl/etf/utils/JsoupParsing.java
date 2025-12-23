package org.unibl.etf.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.unibl.etf.articles.Article;

public class JsoupParsing {
	private static final Logger LOGGER = Logger.getLogger(JsoupParsing.class.getName());
	
	ArrayList<String> urls;
	
	public JsoupParsing() {
		
	}
	
	public JsoupParsing(ArrayList<String> urls) {
		this.urls = urls;
	}
	
	public ArrayList<Article> parseArticles() throws IOException  {
		ArrayList<Article> articles = new ArrayList<Article>();
		
		for(String url : urls) {
			Document doc = Jsoup.connect(url)
			        .userAgent("Mozilla/5.0")
			        .timeout(10000)	//10 sekudni
			        .get();
			
			Elements items = doc.select("li.product-cell.box-product");
			
	        if (items == null) {
	            System.out.println("No product elements found!");
	            return null;
	        }

			for (Element item : items) {
				String code = item.selectFirst(".product-sku").text();
			    String name = item.selectFirst(".product > .product-name > a").text();
			    String price = item.selectFirst(".product-price-value").text().replace("$", "");
			    String image = item.selectFirst(".photo").attr("src");

			    String[] nameSplit = name.strip().split(" ");
			    String manufacturer = nameSplit[0];

			    articles.add(new Article(code, name, manufacturer, Double.parseDouble(price), image));
			}
		}
		LOGGER.info("Number of parsed articles: " + articles.size());
		
		// next two lines are for incase web scraping be rejected or no internet connection
		//writeArticlesToFile(articles, "C:\\Users\\Administrator\\Desktop\\articles.txt");
//		articles = readArticlesFromFile("C:\\Users\\Administrator\\Desktop\\articles.txt");
		
		return articles;
	}
	

	
	public static void writeArticlesToFile(List<Article> articles, String filePath) throws IOException {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        for (Article a : articles) {
	            writer.write(
	                a.getCode() + ";" +
	                a.getName() + ";" +
	                a.getManufacturer() + ";" +
	                a.getPrice() + ";" +
	                a.getImage()
	            );
	            writer.newLine();
	        }
	    }
	}

	public static ArrayList<Article> readArticlesFromFile(String filePath) throws IOException {
	    ArrayList<Article> articles = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        while ((line = reader.readLine()) != null) {
	            if (line.trim().isEmpty()) continue;

	            String[] parts = line.split(";");

	            if (parts.length != 5) continue;

	            String code = parts[0];
	            String name = parts[1];
	            String manufacturer = parts[2];
	            double price = Double.parseDouble(parts[3]);
	            String image = parts[4];

	            articles.add(new Article(code, name, manufacturer, price, image));
	        }
	    }

	    return articles;
	}

    
}
