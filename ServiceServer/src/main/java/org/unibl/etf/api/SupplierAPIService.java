package org.unibl.etf.api;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.order.Order;
import org.unibl.etf.order.OrderDAO;
import org.unibl.etf.order.OrderServer;
import org.unibl.etf.order.OrderService;
import org.unibl.etf.suppliers.Supplier;

@Path("/suppliers")
public class SupplierAPIService {
	
	public SupplierAPIService() { 
	}
	
	@GET
	public Collection<Supplier> getSuppliers(@Context ServletContext ctx) {
	    return OrderServer.suppliers.values();
	}
	
	@GET
	@Path("/orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllOrders(@Context ServletContext ctx) {
	    Collection<Order> orders = OrderDAO.getInstance().getAllOrders();
	    return Response.ok(orders).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/orders")
	public Response sendOrder(@Context ServletContext ctx, Order order) {
	    OrderService orderService = new OrderService(ctx);
	    System.out.println("Kontroler");
	    return Response
	            .status(Response.Status.CREATED)
	            .entity(orderService.sendOrder(order))
	            .build();
	}
}
