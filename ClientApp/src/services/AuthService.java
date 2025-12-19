package services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
import utils.RestClient;

public class AuthService {
	public static final int TCP_PORT = 15000;
	
	public AuthService() {
		
	}
	
	public Client login(String username, String password) throws InvalidLoginException {
		LoginRequest request = new LoginRequest(username, password);
	    HttpURLConnection conn = null;
	    Client result = null;
	    try {
	        conn = RestClient.openConnection("auth/login", true, "POST");
	        RestClient.sendRequest(conn, request);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Client.class);
	        AppSession.getInstance().setCurrentClient(result);
	        
	        //multicast
	        GroupChatReceiver rt = new GroupChatReceiver(username);
			rt.start();	
			
			GroupChatSender st = new GroupChatSender(username);
	        AppSession.getInstance().setGroupChatSender(st);
	        
	        //unicast
	        InetAddress addr = InetAddress.getByName("127.0.0.1");
            Socket sock = new Socket(addr, TCP_PORT);
            
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
	
	public Client registration(ClientRequest request, Vehicle vehicle) throws InvalidLoginException {
		HttpURLConnection conn = null;
	    Client result = null;
	    Registration registrationRequest = new Registration(request, vehicle);
	    try {
	        conn = RestClient.openConnection("auth/registration", true, "POST");
	        RestClient.sendRequest(conn, registrationRequest);
	        String response = RestClient.readResponse(conn);
	        result = RestClient.convertFromJSON(response, Client.class);
	        AppSession.getInstance().setCurrentClient(result);
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	    return result;
	}
}
