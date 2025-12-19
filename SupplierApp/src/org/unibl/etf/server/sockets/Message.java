package org.unibl.etf.server.sockets;

import java.util.ArrayList;

import org.unibl.etf.utils.AppSession;

public class Message<T> {
    private MessageType type;
    private String supplierName;
    private ArrayList<T> payload;

    public Message() {
        super();
    }

    public Message(MessageType type, String supplierName, ArrayList<T> payload) {
        super();
        this.type = type;
        this.supplierName = supplierName;
        this.payload = payload;
    }
    
    public Message(MessageType type, ArrayList<T> payload) {
        super();
        this.type = type;
        this.supplierName = AppSession.getInstance().getSupplierName();
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

    public ArrayList<T> getPayload() {
        return payload;
    }

    public void setPayload(ArrayList<T> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message [type=" + type + ", supplierName=" + supplierName + ", payload=" + payload + "]";
    }
}
