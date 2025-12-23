package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.logging.Logger;

import exceptions.ServerError;
import models.Appointment;
import utils.Config;
import utils.JSONConversion;
import utils.RestClient;

public class AppointmentService {
	private static final Logger LOGGER = Logger.getLogger(AppointmentService.class.getName());
	
	public AppointmentService() {
		
	}
	
	public ArrayList<Appointment> getAllApointments() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.appointments"), false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			LOGGER.info("Servicer is fetching for all appointmnets");
			return JSONConversion.convertArrayList(jsonResponse, Appointment.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public void deleteAppointment(String id) throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.appointments") + "/" + id, true, "DELETE");
			RestClient.sendRequest(conn, null);
			LOGGER.info("Servicer deleted appointmnet " + id);
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	}
	
	public Appointment updateAppointment(String id, Appointment update) throws ServerError {
		HttpURLConnection conn = null;
		Appointment result = null;
	    try {
	        conn = RestClient.openConnection(Config.get("rest.endpoint.appointments") + "/" + id, true, "PUT");
	        RestClient.sendRequest(conn, update);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Appointment.class);
	        LOGGER.info("Servicer updated appointmnet " + id);
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
}
