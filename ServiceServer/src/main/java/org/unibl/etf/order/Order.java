package org.unibl.etf.order;

import java.util.UUID;

public class Order {
	private String id;
	private String supplier;
	private Integer quantity;
	private OrderStatus status;
	private String articleId;
	
	public Order() {
		
	}

	public Order(String supplier, Integer quantity, String articleId) {
		super();
		this.id = UUID.randomUUID().toString();
		this.supplier = supplier;
		this.quantity = quantity;
		this.articleId = articleId;
		this.status = OrderStatus.WAITING;
	}
	
	public Order(OrderRequest order) {
		this(order.getSupplier(), order.getQuantity(), order.getArticleId());
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
}
