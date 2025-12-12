package org.unibl.etf.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.unibl.etf.articles.Article;

public class JsoupParsing {
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
			        .timeout(10000)
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
		System.out.println("Number of parsed articles: " + articles.size());
		return articles;
	}
	

    
}
