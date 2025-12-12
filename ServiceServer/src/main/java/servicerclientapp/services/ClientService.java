package servicerclientapp.services;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import servicerclientapp.exceptions.ServerError;
import servicerclientapp.models.Client;
import servicerclientapp.utils.JSONConversion;
import servicerclientapp.utils.RestClient;



/**
 * Use for client management from service application. Can get all clients, approve, block and delete client account.
 */
public class ClientService {
	public ClientService() {
		
	}
	
	public ArrayList<Client> getAllClients() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection("clients", false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			return JSONConversion.convertArrayList(jsonResponse, Client.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	/**
	 * Use for approving and blocking client's account. 
	 * @param clientId client's id
	 * @param updatedClient client's object with updated states, for this purpose field status
	 * @return updated client
	 * @throws ServerError
	 */
	public Client updateClient(String clientId, Client updatedClient) throws ServerError {
		HttpURLConnection conn = null;
	    Client result = null;
	    try {
	        conn = RestClient.openConnection("clients/" + clientId, true, "PUT");
	        RestClient.sendRequest(conn, updatedClient);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Client.class);
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	/**
	 * Use for deleting client's account from Service application
	 * @param clientId
	 * @throws ServerError
	 */
	public void deleteClient(String clientId) throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection("clients/" + clientId, true, "DELETE");
			RestClient.sendRequest(conn, null);
			System.out.println(RestClient.readResponse(conn));
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	}
}
