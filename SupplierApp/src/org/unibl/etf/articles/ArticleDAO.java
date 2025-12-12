package org.unibl.etf.articles;

import java.io.IOException;
import java.util.ArrayList;

import org.unibl.etf.utils.JsoupParsing;

public class ArticleDAO {
	private static ArrayList<Article> articles;
	
	private ArrayList<String> urls;
	
	public ArticleDAO() {
		addUrls();
		if(articles == null || articles.isEmpty()) {
			try {
				articles = new JsoupParsing(urls).parseArticles();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<Article> getArticles() {
		return articles;
	}
	
	public ArrayList<Article> addArticles(ArrayList<Article> newArticles) {
		articles.addAll(newArticles);
		return articles;
	}
	
	private void addUrls() {
		urls = new ArrayList<String>();
		urls.add("https://eeuroparts.com/brake-system/brake-pads/");
		urls.add("https://eeuroparts.com/engine/oil-filters/");
		urls.add("https://eeuroparts.com/ignition/spark-plugs/");
		urls.add("https://eeuroparts.com/fuel-delivery/fuel-pumps/");
		urls.add("https://eeuroparts.com/belts-and-cooling/thermostats/");
		urls.add("https://eeuroparts.com/electrical-and-lighting/headlights/");
		urls.add("https://eeuroparts.com/electrical-and-lighting/fuses/");
	}
}
