package org.unibl.etf.articles;

import java.io.Serializable;
import java.util.Objects;

public class Article implements Serializable {
	private static final long serialVersionUID = 2544867697380729261L;

	private String code;
	private String name;
	private String manufacturer;
	private double price;
	private String image;
	
	public Article() {
		super();
	}
	
	public Article(String code, String name, String manufacturer, double price, String image) {
		super();
		this.code = code;
		this.name = name;
		this.manufacturer = manufacturer;
		this.price = price;
		this.image = image;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Article [code=" + code + ", name=" + name + ", manufacturer=" + manufacturer + ", price=" + price
				+ "€, image=" + image + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		return Objects.equals(code, other.code);
	}
}
