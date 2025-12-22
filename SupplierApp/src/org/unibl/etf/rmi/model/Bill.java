package org.unibl.etf.rmi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.unibl.etf.orders.Order;

public class Bill implements Serializable {
	private static final long serialVersionUID = -5800692443915460190L;
	
	private String supplierName;
	private Order order;
	private BigDecimal amountWithoutTax;
	private BigDecimal tax;
	private BigDecimal total;
	
	public Bill() {
		super();
	}

	public Bill(String supplierName, Order order, BigDecimal amountWithoutTax) {
		super();
		this.supplierName = supplierName;
		this.order = order;
		this.amountWithoutTax = amountWithoutTax;
		BigDecimal taxRate = new BigDecimal("0.17");
		this.tax = amountWithoutTax.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
		this.total = amountWithoutTax.add(this.tax);
	}

	public String getSupplierName() {
		return supplierName;
	}
	
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public BigDecimal getAmountWithoutTax() {
		return amountWithoutTax;
	}
	
	public void setAmountWithoutTax(BigDecimal amountWithoutTax) {
		this.amountWithoutTax = amountWithoutTax;
	}
	
	public BigDecimal getTax() {
		return tax;
	}
	
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
