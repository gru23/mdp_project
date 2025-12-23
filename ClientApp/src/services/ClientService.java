package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.logging.Logger;

import exceptions.InvalidLoginException;
import models.Appointment;
import models.requests.AppointmentRequest;
import utils.AppSession;
import utils.Config;
import utils.JSONConversion;
import utils.RestClient;

public class ClientService {
	private static final Logger LOGGER = Logger.getLogger(ClientService.class.getName());
	
	public ClientService() {
		
	}
	
	public Appointment makeAppointment(AppointmentRequest newAppointment) throws InvalidLoginException {
		HttpURLConnection conn = null;
	    Appointment result = null;
	    try {
	        conn = RestClient.openConnection(Config.get("rest.endpoint.appointments"), true, "POST");
	        RestClient.sendRequest(conn, newAppointment);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Appointment.class);
	        LOGGER.info(String.format("Client %s made appointment", newAppointment.getClientId()));
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public ArrayList<Appointment> appointmentsHistory() throws InvalidLoginException{
		HttpURLConnection conn = null;
		try {
			String clientId = AppSession.getInstance().getCurrentClient().getId();
			conn = RestClient.openConnection(Config.get("rest.endpoint.appointments.clients") + clientId, false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			return JSONConversion.convertArrayList(jsonResponse, Appointment.class);
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	}
}
