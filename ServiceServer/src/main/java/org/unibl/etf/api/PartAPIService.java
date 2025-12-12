package org.unibl.etf.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.parts.PartEntity;
import org.unibl.etf.parts.PartService;
import org.unibl.etf.util.Message;

@Path("/parts")
public class PartAPIService {
	private final PartService service;
	
	public PartAPIService() {
		service = new PartService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		return Response.ok(service.readAll()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public Response getById(@PathParam("id") String id) {
		try {
			return Response
					.ok(service.read(id))
					.build();
		} catch(NotFoundException e) {
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(new Message("Part not found!"))
					.build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(PartEntity part) {
		return Response
				.status(Response.Status.CREATED)
				.entity(service.add(part))
				.build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		try {
			service.delete(id);
			return Response.noContent().build();
		} catch(NotFoundException e) {
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(new Message("Part not found!"))
					.build();
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response update(@PathParam("id") String id, PartEntity part) {
		try {
			return Response
					.ok(service.update(id, part))
					.build();
		} catch(NotFoundException e) {
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(new Message("Part not found!"))
					.build();
		}
	}
}
