package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Optional;

import exceptions.ServerError;
import models.Part;
import utils.JSONConversion;
import utils.RestClient;

public class SparePartService {
	
	public SparePartService() {
		
	}
	
	public ArrayList<Part> getAllParts() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection("parts", false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
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
	        conn = RestClient.openConnection("parts/", true, "POST");
	        RestClient.sendRequest(conn, newPart);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Part.class);
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public Part updatePart(String code, Part updatedPart) throws ServerError {
		HttpURLConnection conn = null;
	    Part result = null;
	    try {
	        conn = RestClient.openConnection("parts/" + code, true, "PUT");
	        RestClient.sendRequest(conn, updatedPart);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Part.class);
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public void deletePart(String code) throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection("parts/" + code, true, "DELETE");
			RestClient.sendRequest(conn, null);
			System.out.println(RestClient.readResponse(conn));
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	}
}
