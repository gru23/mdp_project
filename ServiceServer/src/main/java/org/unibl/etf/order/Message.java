package org.unibl.etf.order;

import java.util.ArrayList;

import org.unibl.etf.order.enums.MessageType;

public class Message {
    private MessageType type;
    private String supplierName;
    private ArrayList<OrderingArticle> payload;

    public Message() {
        super();
    }

    public Message(MessageType type, String supplierName, ArrayList<OrderingArticle> payload) {
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

    public ArrayList<OrderingArticle> getPayload() {
        return payload;
    }

    public void setPayload(ArrayList<OrderingArticle> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message [type=" + type + ", supplierName=" + supplierName + ", payload=" + payload + "]";
    }
}
