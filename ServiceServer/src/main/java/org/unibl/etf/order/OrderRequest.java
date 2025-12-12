package org.unibl.etf.order;

public class OrderRequest {
	private String supplier;
	private Integer quantity;
	private String articleId;
	
	public OrderRequest() {
		super();
	}
	public OrderRequest(String supplier, Integer quantity, String articleId) {
		super();
		this.supplier = supplier;
		this.quantity = quantity;
		this.articleId = articleId;
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
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	
}
