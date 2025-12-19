package org.unibl.etf.server.articles;

import java.util.ArrayList;

import org.unibl.etf.articles.Article;

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
	
	public Article addArticle(Article newArticle) {
		return articleDAO.addArticle(newArticle);
	}
}
