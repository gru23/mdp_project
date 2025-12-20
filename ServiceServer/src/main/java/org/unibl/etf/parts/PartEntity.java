package org.unibl.etf.parts;

import java.io.Serializable;

import org.unibl.etf.order.OrderingArticle;

public class PartEntity implements Serializable  {
	private static final long serialVersionUID = 3389606408315366151L;
	
	private String code;
	private String name;
	private String manufacturer;
	private double price;
	private int quantity;
	private String description;
	
	public PartEntity() {
		super();
	}

	public PartEntity(String code, String name, String manufacturer, double price, int quantity, String description) {
		super();
		this.code = code;
		this.name = name;
		this.manufacturer = manufacturer;
		this.price = price;
		this.quantity = quantity;
		this.description = description;
	}
	
	public PartEntity(OrderingArticle article) {
		super();
		this.code = article.getCode();
		this.name = article.getName();
		this.manufacturer = article.getManufacturer();
		this.price = article.getPrice();
		this.quantity = article.getQuanity();
		this.description = "";
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Part [code=" + code + ", name=" + name + ", manufacturer=" + manufacturer + ", price=" + price
				+ ", quantity=" + quantity + ", description=" + description + "]";
	}
}
