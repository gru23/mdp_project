package org.unibl.etf.order;

public class OrderService {
	
	public Order sendOrder(OrderRequest request) {
		//dio sa serverom dobavljaca
		//provjera request-a npr. postoji li dobavljac koji je naveo
		return new Order(request);
	}
}
