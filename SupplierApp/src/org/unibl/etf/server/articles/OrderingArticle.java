package org.unibl.etf.server.articles;

import org.unibl.etf.articles.Article;

public class OrderingArticle {
	private String code;
	private String name;
	private String manufacturer;
	private double price;
	private int quanity;
	
	public OrderingArticle() {
		
	}

	public OrderingArticle(String code, String name, String manufacturer, double price, int quantity) {
		super();
		this.code = code;
		this.name = name;
		this.manufacturer = manufacturer;
		this.price = price;
		this.quanity = quantity;
	}
	
	public OrderingArticle(Article article) {
		super();
		this.code = article.getCode();
		this.name = article.getName();
		this.manufacturer = article.getManufacturer();
		this.price = article.getPrice();
		this.quanity = 0;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuanity() {
		return quanity;
	}

	public void setQuanity(int quanity) {
		this.quanity = quanity;
	}

	@Override
	public String toString() {
		return "OrderingArticle [code=" + code + ", name=" + name + ", manufacturer=" + manufacturer + ", price="
				+ price + "]";
	}
}

