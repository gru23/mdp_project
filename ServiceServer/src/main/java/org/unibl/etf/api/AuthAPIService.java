package org.unibl.etf.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.unibl.etf.auth.AuthService;
import org.unibl.etf.auth.RegistrationDTO;
import org.unibl.etf.client.login.LoginRequest;
import org.unibl.etf.exceptions.DuplicateException;
import org.unibl.etf.exceptions.InternalServerError;
import org.unibl.etf.exceptions.InvalidCredentialsException;
import org.unibl.etf.util.Message;


@Path("/auth")
public class AuthAPIService {
	private final AuthService service;
	
	public AuthAPIService() {
		service = new AuthService();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login(LoginRequest request) {
		try {
			return Response
					.ok(service.login(request))
					.build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		} catch(InvalidCredentialsException e) {
			return Response
					.status(Response.Status.UNAUTHORIZED)
					.entity(new Message(e.getMessage()))
					.build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/registration")
	public Response registration(RegistrationDTO request) {
		try {
			return Response
					.ok(service.registration(request.getRequest(), request.getVehicle()))
					.build();
		} catch(InternalServerError e) {
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		} catch(DuplicateException e) {
			return Response
					.status(Response.Status.CONFLICT)
					.entity(new Message(e.getMessage()))
					.build();
		} //could be added 400 Bad Request in case if some filed are invalid (email format, length...)
	}
}
