package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import exceptions.ServerError;
import models.Part;
import utils.Config;
import utils.JSONConversion;
import utils.RestClient;

public class SparePartService {
	private static final Logger LOGGER = Logger.getLogger(SparePartService.class.getName());
	
	public SparePartService() {
		
	}
	
	public ArrayList<Part> getAllParts() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.parts"), false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			LOGGER.info("Servicer is fetching for all parts");
			return JSONConversion.convertArrayList(jsonResponse, Part.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public Optional<Part> getPartByCode(String code) throws ServerError {
		ArrayList<Part> parts = getAllParts();
		return parts.stream()
						.filter(p -> code.equals(p.getCode()))
						.findFirst();
	}
	
	public Part createPart(Part newPart) throws ServerError {
		HttpURLConnection conn = null;
	    Part result = null;
	    try {
	        conn = RestClient.openConnection(Config.get("rest.endpoint.parts") + "/", true, "POST");
	        RestClient.sendRequest(conn, newPart);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Part.class);
	        LOGGER.info("Servicer created a new part " + newPart.getCode());
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public Part updatePart(String code, Part updatedPart) throws ServerError {
		HttpURLConnection conn = null;
	    Part result = null;
	    try {
	        conn = RestClient.openConnection(Config.get("rest.endpoint.parts") +"/" + code, true, "PUT");
	        RestClient.sendRequest(conn, updatedPart);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Part.class);
	        LOGGER.info("Servicer updated a new part " + updatedPart.getCode());
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public void deletePart(String code) throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.parts") + "/" + code, false, "DELETE");
			RestClient.readResponse(conn);
			LOGGER.info("Servicer deleted part " + code);
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	}
}
