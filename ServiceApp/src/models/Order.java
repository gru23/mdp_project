package models;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import enums.OrderStatus;

public class Order {
	private String id;
	private String supplier;
	private String date;
	private OrderStatus status;
	private ArrayList<OrderingArticle> articles;
	
	public Order() {
		super();
	}
	
	public Order(String id, String supplier, String date, OrderStatus status, ArrayList<OrderingArticle> articles) {
		super();
		this.id = id;
		this.supplier = supplier;
		this.date = date;
		this.status = status;
		this.articles = articles;
	}

	public Order(String supplier, ArrayList<OrderingArticle> articles) {
		super();
		this.id = UUID.randomUUID().toString();
		this.date = LocalDate.now().toString();
		this.supplier = supplier;
		this.status = OrderStatus.WAITING;
		this.articles = articles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public ArrayList<OrderingArticle> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<OrderingArticle> articles) {
		this.articles = articles;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", supplier=" + supplier + ", date=" + date + ", status=" + status + ", articles="
				+ articles + "]";
	}	
}
