package org.unibl.etf.suppliers;

import java.util.ArrayList;

import org.unibl.etf.order.OrderingArticle;

public class Supplier {
	private String name;
	private ArrayList<OrderingArticle> articles;
	
	public Supplier() {
		super();
	}

	public Supplier(String name) {
		super();
		this.name = name;
		this.articles = new ArrayList<OrderingArticle>();
	}


	public Supplier(String supplierName, ArrayList<OrderingArticle> payload) {
		super();
		this.name = supplierName;
		this.articles = payload;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<OrderingArticle> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<OrderingArticle> articles) {
		this.articles = articles;
	}
	
	@Override
	public String toString() {
		return "Supplier [name=" + name + "]";
	}
}
