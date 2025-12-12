package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import exceptions.InvalidLoginException;
import models.Appointment;
import models.requests.AppointmentRequest;
import utils.AppSession;
import utils.JSONConversion;
import utils.RestClient;

public class ClientService {
	public ClientService() {
		
	}
	
	public Appointment makeAppointment(AppointmentRequest newAppointment) throws InvalidLoginException {
		HttpURLConnection conn = null;
	    Appointment result = null;
	    try {
	        conn = RestClient.openConnection("appointments", true, "POST");
	        RestClient.sendRequest(conn, newAppointment);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Appointment.class);
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public ArrayList<Appointment> appointmentsHistory() throws InvalidLoginException{
		HttpURLConnection conn = null;
		try {
			String clientId = AppSession.getInstance().getCurrentClient().getId();
			conn = RestClient.openConnection("appointments/clients/" + clientId, false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			return JSONConversion.convertArrayList(jsonResponse, Appointment.class);
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	}
}
