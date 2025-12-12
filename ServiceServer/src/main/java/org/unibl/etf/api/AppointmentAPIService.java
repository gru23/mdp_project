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

import org.unibl.etf.appointment.AppointmentEntity;
import org.unibl.etf.appointment.AppointmentRequest;
import org.unibl.etf.appointment.AppointmentService;
import org.unibl.etf.exceptions.AppointmentConflictException;
import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.util.Message;

@Path("/appointments")
public class AppointmentAPIService {
	AppointmentService service;
	
	public AppointmentAPIService() {
		service = new AppointmentService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		try {
			return Response.ok(service.getAll()).build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/status/{status}")
	public Response getAllByStatus(@PathParam("status") String status) {
		try {
			return Response.ok(service.getAllByStatus(status)).build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/clients/{clientid}")
	public Response getAllByClientId(@PathParam("clientid") String clientId) {
		try {
			return Response.ok(service.getAllByClientId(clientId)).build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("/{id}")
	public Response getById(@PathParam("id") String id) {
		try {
			return Response
					.ok(service.getById(id))
					.build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		} catch(NotFoundException e) {
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(new Message("Appointment not found!"))
					.build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(AppointmentRequest appointment) {
		try {
			return Response
					.status(Response.Status.CREATED)
					.entity(service.add(appointment))
					.build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		} catch(AppointmentConflictException e) {
			return Response
					.status(Response.Status.CONFLICT)
					.entity(new Message(e.getMessage()))
					.build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		try {
			service.delete(id);
			return Response.noContent().build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		} catch(NotFoundException e) {
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(new Message("Appointment not found!"))
					.build();
		}
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response update(@PathParam("id") String id, AppointmentEntity appointment) {
		try {
			return Response
					.ok(service.update(id, appointment))
					.build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		} catch(NotFoundException e) {
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(new Message("Appointment not found!"))
					.build();
		}
	}
}
