package servicerclientapp.services;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import servicerclientapp.exceptions.ServerError;
import servicerclientapp.models.Appointment;
import servicerclientapp.utils.JSONConversion;
import servicerclientapp.utils.RestClient;



public class AppointmentService {
	public AppointmentService() {
		
	}
	
	public ArrayList<Appointment> getAllApointments() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection("appointments", false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			return JSONConversion.convertArrayList(jsonResponse, Appointment.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public Appointment updateAppointment(String id, Appointment update) throws ServerError {
		HttpURLConnection conn = null;
		Appointment result = null;
	    try {
	        conn = RestClient.openConnection("appointments/" + id, true, "PUT");
	        RestClient.sendRequest(conn, update);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Appointment.class);
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
}
