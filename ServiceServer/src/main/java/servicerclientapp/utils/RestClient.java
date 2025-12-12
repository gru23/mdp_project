package servicerclientapp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.google.gson.Gson;

import servicerclientapp.exceptions.ServerError;


public class RestClient {
	public static final String BASE_URL = "http://localhost:8080/ServiceServer/api/";
	
	private static final Gson gson = new Gson();
	
	public static HttpURLConnection openConnection(String realtivePathURL, boolean hasBody, String requestType) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(BASE_URL + realtivePathURL);
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(hasBody);
	        conn.setRequestMethod(requestType);
	        conn.setRequestProperty("Content-Type", "application/json");
		} catch(IOException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void sendRequest(HttpURLConnection conn, Object data) {
        try (OutputStream os = conn.getOutputStream()) {
        	if(data != null) {
        		JSONObject input = new JSONObject(data);
        		os.write(input.toString().getBytes());
        	}
        	os.flush();
        } catch(IOException e) {
        	e.printStackTrace();
        }
	}
	
	public static String readResponse(HttpURLConnection conn) throws ServerError {
		StringBuilder response = new StringBuilder();
		try {
			int responseCode = conn.getResponseCode();
			try (BufferedReader br = new BufferedReader(
	                new InputStreamReader(
	                        responseCode >= 200 && responseCode < 300
	                                ? conn.getInputStream()
	                                : conn.getErrorStream()
	                ))) {

	            String line;
	            while ((line = br.readLine()) != null) {
	                response.append(line);
	            }
	            if (responseCode >= 300) {
	                String serverMessage = response.toString();
	                ServerErrorMessage error = gson.fromJson(serverMessage, ServerErrorMessage.class);
	                throw new ServerError("Server returned error: " + error.getContent());
	            }
	        }
		} catch(IOException e) {
			e.printStackTrace();
		}
		return response.toString();
	}
	
	public static <T> T convertFromJSON(String jsonString, Class<T> clazz) {
	    return gson.fromJson(jsonString, clazz);
	}
}
