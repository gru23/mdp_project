package org.unibl.etf.auth;

import org.unibl.etf.client.ClientRequest;
import org.unibl.etf.vehicle.VehicleEntity;

public class RegistrationDTO {
	private ClientRequest request;
    private VehicleEntity vehicle;
    
	public RegistrationDTO() {
		super();
	}
	
	public RegistrationDTO(ClientRequest request, VehicleEntity vehicle) {
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
	public VehicleEntity getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleEntity vehicle) {
		this.vehicle = vehicle;
	}
}
