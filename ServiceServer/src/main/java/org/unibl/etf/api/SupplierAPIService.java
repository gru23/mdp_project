package org.unibl.etf.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.order.OrderRequest;
import org.unibl.etf.order.OrderService;
import org.unibl.etf.suppliers.SupplierService;

@Path("/suppliers")
public class SupplierAPIService {
	private final SupplierService service;
	private final OrderService orderService;
	
	public SupplierAPIService() {
		this.service = new SupplierService();
		this.orderService = new OrderService(); 
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		return Response.ok(service.getAll()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/orders")
	public Response sendOrder(OrderRequest order) {
		return Response
				.status(Response.Status.CREATED)
				.entity(orderService.sendOrder(order))
				.build();
	}

}
