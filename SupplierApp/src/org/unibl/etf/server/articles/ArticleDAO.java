package org.unibl.etf.server.articles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.unibl.etf.articles.Article;
import org.unibl.etf.utils.Config;
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
	            ArrayList<Article> parsedArticles = new JsoupParsing(getRandomUrls()).parseArticles();
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
		urls.add(Config.get("articles.brakes.url"));
		urls.add(Config.get("articles.filters.url"));
		urls.add(Config.get("articles.sparks.url"));
		urls.add(Config.get("articles.pumps.url"));
		urls.add(Config.get("articles.thermostats.url"));
		urls.add(Config.get("articles.headlights.url"));
		urls.add(Config.get("articles.fuses.url"));
	}
	
	private ArrayList<String> getRandomUrls() {
		Random rand = new Random();
		ArrayList<String> urlsCopy = new ArrayList<String>();
		int randomNumberOfUrls = rand.nextInt(urls.size()) + 1;
		for(int i = 0; i < randomNumberOfUrls; i++) {
			urlsCopy.add(urls.get(rand.nextInt(urls.size())));
		}
		return urlsCopy
				.stream()
	            .distinct()
	            .collect(Collectors.toCollection(ArrayList::new));
	}
}
