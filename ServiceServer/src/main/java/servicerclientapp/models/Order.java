package servicerclientapp.models;

import servicerclientapp.enums.OrderStatus;

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
		this.supplier = supplier;
		this.quantity = quantity;
		this.articleId = articleId;
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

	@Override
	public String toString() {
		return "Order [id=" + id + ", supplier=" + supplier + ", quantity=" + quantity + ", status=" + status
				+ ", articleId=" + articleId + "]";
	}
	
	
}
