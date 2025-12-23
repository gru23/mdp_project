package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.gson.Gson;

import exceptions.InvalidLoginException;

public class RestClient {
	private static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());
	
	public static final String BASE_URL = Config.get("rest.base.url");
	
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
			LOGGER.log(Level.SEVERE, "I/O Exception during opening HTTP connection.");
		}
		return conn;
	}
	
	public static void sendRequest(HttpURLConnection conn, Object data) {
		JSONObject input = new JSONObject(data);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(input.toString().getBytes());
            os.flush();
        } catch(IOException e) {
        	LOGGER.log(Level.SEVERE, "I/O Exception during sending HTTP request.");
        }
	}
	
	public static String readResponse(HttpURLConnection conn) throws InvalidLoginException {
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
	                ServerError error = gson.fromJson(serverMessage, ServerError.class);
	                throw new InvalidLoginException("Server returned error: " + error.getContent());
	            }
	        }
		} catch(IOException e) {
			LOGGER.log(Level.SEVERE, "I/O Exception during reading HTTP response.");
		}
		return response.toString();
	}
	
	public static <T> T convertFromJSON(String jsonString, Class<T> clazz) {
	    return gson.fromJson(jsonString, clazz);
	}
}
