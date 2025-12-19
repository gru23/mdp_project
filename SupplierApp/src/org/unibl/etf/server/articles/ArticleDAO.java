package org.unibl.etf.server.articles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.unibl.etf.articles.Article;
import org.unibl.etf.utils.JsoupParsing;


public class ArticleDAO {
	private static Map<String, Article> articles;
	
	private ArrayList<String> urls;
	
	public ArticleDAO() {
		addUrls();
		if(articles == null || articles.isEmpty()) {
//			try {
//				articles = new JsoupParsing(urls).parseArticles();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			try {
	            ArrayList<Article> parsedArticles = new JsoupParsing(urls).parseArticles();
	            articles = new LinkedHashMap<>();
	            if (parsedArticles != null) {
	                for (Article a : parsedArticles) {
	                    articles.put(a.getCode(), a);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            articles = new LinkedHashMap<>();
	        }
		}
	}
	
	public ArrayList<Article> getArticles() {
		return new ArrayList<>(articles.values());
	}
	
	public ArrayList<Article> addArticles(ArrayList<Article> newArticles) {
	    newArticles.forEach(a -> articles.put(a.getCode(), a));
	    return new ArrayList<>(articles.values());
	}
	
	public Article addArticle(Article newArticle) {
		articles.put(newArticle.getCode(), newArticle);
		return newArticle;
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
