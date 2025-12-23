package org.unibl.etf.order;

import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.unibl.etf.order.enums.MessageType;

public class OrderService {
	private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
	
	private OrderServer orderServer;
	
	public OrderService() {
		
	}
	
	public OrderService(ServletContext context) {
        this.orderServer = (OrderServer) context.getAttribute("orderServer");
    }
	
	public Order sendOrder(Order request) {
        if (request == null || request.getSupplier() == null) {
            return request;
        }

        MessageOrder message = new MessageOrder(
        		MessageType.ORDER_REQUEST,
        		request.getSupplier(),
        		request
        );
        System.out.println("Order stig'o na ServiceServer, klasa OrderServie");
        OrderDAO.getInstance().addOrder(request);
        orderServer.sendOrder(request.getSupplier(), message);
        LOGGER.info("Order forwarded");
        return request;
    }
}
