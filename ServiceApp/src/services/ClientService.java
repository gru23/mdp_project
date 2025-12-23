package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.logging.Logger;

import exceptions.ServerError;
import models.Client;
import utils.Config;
import utils.JSONConversion;
import utils.RestClient;

/**
 * Use for client management from service application. Can get all clients, approve, block and delete client account.
 */
public class ClientService {
	private static final Logger LOGGER = Logger.getLogger(ClientService.class.getName());
	
	public ClientService() {
		
	}
	
	public ArrayList<Client> getAllClients() throws ServerError {
		HttpURLConnection conn = null;
		try {
			conn = RestClient.openConnection(Config.get("rest.endpoint.clients"), false, "GET");
			String jsonResponse = RestClient.readResponse(conn);
			LOGGER.info("Servicer is fetching for all clients");
			return JSONConversion.convertArrayList(jsonResponse, Client.class);
		} finally {
			if(conn != null) conn.disconnect();
		}
	}
	
	public Client getClientByUsername(String username) throws ServerError {
		ArrayList<Client> clients = getAllClients();
		LOGGER.info("Servicer is fetching client by username " + username);
		return clients.stream()
						.filter(c -> username.equals(c.getUsername()))
						.findFirst()
						.get();
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
	        conn = RestClient.openConnection(Config.get("rest.endpoint.clients") + "/" + clientId, true, "PUT");
	        RestClient.sendRequest(conn, updatedClient);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Client.class);
	        LOGGER.info("Servicer updated client " + updatedClient.getUsername());
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
			conn = RestClient.openConnection(Config.get("rest.endpoint.clients") + "/" + clientId, true, "DELETE");
			RestClient.sendRequest(conn, null);
			LOGGER.info("Servicer updated client " + clientId);
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	}
}
