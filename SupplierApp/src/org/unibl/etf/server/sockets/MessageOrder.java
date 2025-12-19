package org.unibl.etf.server.sockets;

import org.unibl.etf.orders.Order;

public class MessageOrder {
	private MessageType type;
    private String supplierName;
    private Order payload;

    public MessageOrder() {
        super();
    }

    public MessageOrder(MessageType type, String supplierName, Order payload) {
        super();
        this.type = type;
        this.supplierName = supplierName;
        this.payload = payload;
    }

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Order getPayload() {
		return payload;
	}

	public void setPayload(Order payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "MessageOrder [type=" + type + ", supplierName=" + supplierName + ", payload=" + payload + "]";
	}
}