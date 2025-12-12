package org.unibl.etf.suppliers;

import java.util.ArrayList;

public class Supplier {
	private String name;
	private ArrayList<Article> articles;
	
	public Supplier() {
		super();
	}

	public Supplier(String name) {
		super();
		this.name = name;
		this.articles = new ArrayList<Article>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Article> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}
	
	@Override
	public String toString() {
		return "Supplier [name=" + name + "]";
	}
}
