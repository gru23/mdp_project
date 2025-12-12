package org.unibl.etf.articles;

import java.util.ArrayList;

public class ArticleService {
	ArticleDAO articleDAO;
	
	public ArticleService() {
		this.articleDAO = new ArticleDAO();
	}
	
	public ArrayList<Article> getAllArticles() {
		return articleDAO.getArticles();
	}
	
	public ArrayList<Article> addArticles(ArrayList<Article> newArticles) {
		return articleDAO.addArticles(newArticles);
	}
}
