package services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import chat.GroupChatReceiver;
import chat.GroupChatSender;
import chat.UnicastChatReceiver;
import chat.UnicastChatSender;
import exceptions.InvalidLoginException;
import models.Client;
import models.Vehicle;
import models.dto.Registration;
import models.requests.ClientRequest;
import models.requests.LoginRequest;
import utils.AppSession;
import utils.Config;
import utils.RestClient;

public class AuthService {
	private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
	
	private static final String HOST = Config.get("chat.unicast.host");
	private static final int PORT = Config.getInt("chat.unicast.port");
	private static final String KEY_STORE_PATH = Config.get("keystore.path");
	private static final String KEY_STORE_PASSWORD = Config.get("keystore.password");
	
	public AuthService() {
		
	}
	
	public Client login(String username, String password) throws InvalidLoginException {
		LOGGER.info("Login attempt by client: " + username);
		LoginRequest request = new LoginRequest(username, password);
	    HttpURLConnection conn = null;
	    Client result = null;
	    try {
	        conn = RestClient.openConnection(Config.get("rest.endpoint.login"), true, "POST");
	        RestClient.sendRequest(conn, request);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Client.class);
	        AppSession.getInstance().setCurrentClient(result);
	        LOGGER.info("Succesuful login: " + username);
	        
	        //multicast
	        GroupChatReceiver rt = new GroupChatReceiver(username);
			rt.start();	
			
			GroupChatSender st = new GroupChatSender(username);
	        AppSession.getInstance().setGroupChatSender(st);
	        
	        //unicast
	        System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
			System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);

			SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			
	        SSLSocket sock = (SSLSocket) sf.createSocket(HOST, PORT);
            
            PrintWriter out = new PrintWriter(
            	    new BufferedWriter(
            	        new OutputStreamWriter(sock.getOutputStream())
            	    ), true
            	);

            	// HANDSHAKE – SAMO JEDNOM
            	out.println(username);
            
            UnicastChatReceiver ucr = new UnicastChatReceiver(sock);
            ucr.start();
            
	        UnicastChatSender ucs = new UnicastChatSender(sock, username);
	        AppSession.getInstance().setUnicastChatSender(ucs);
	        
	    } catch (UnknownHostException e) {
	    	LOGGER.log(Level.SEVERE, "Unknown host for unicast chat. Host: " + HOST, e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "I/O Exception during loging client: " + username, e);
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public Client registration(ClientRequest request, Vehicle vehicle) throws InvalidLoginException {
		LOGGER.info("Registration attempt for client: " + request.getUsername());
		HttpURLConnection conn = null;
	    Client result = null;
	    Registration registrationRequest = new Registration(request, vehicle);
	    try {
	        conn = RestClient.openConnection(Config.get("rest.endpoint.registration"), true, "POST");
	        RestClient.sendRequest(conn, registrationRequest);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Client.class);
	        AppSession.getInstance().setCurrentClient(result);
	        LOGGER.info("Succesuful registration: " + request.getUsername());
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
}
