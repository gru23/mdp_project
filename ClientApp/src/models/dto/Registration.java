package models.dto;

import models.Vehicle;
import models.requests.ClientRequest;

public class Registration {
	private ClientRequest request;
    private Vehicle vehicle;
    
	public Registration() {
		super();
	}
	
	public Registration(ClientRequest request, Vehicle vehicle) {
		super();
		this.request = request;
		this.vehicle = vehicle;
	}
	
	public ClientRequest getRequest() {
		return request;
	}
	public void setRequest(ClientRequest request) {
		this.request = request;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
    
    

}
