package org.unibl.etf.invoice;

import java.io.Serializable;
import java.util.ArrayList;

import org.unibl.etf.appointment.enums.AppointmentType;
import org.unibl.etf.client.Client;
import org.unibl.etf.parts.PartEntity;
import org.unibl.etf.vehicle.VehicleEntity;

public class Invoice implements Serializable {
	private static final long serialVersionUID = -8747087730413647666L;
	
	private ArrayList<PartEntity> changedParts;
	private AppointmentType typeOfService;
	private Client client;
	private VehicleEntity vehicle;
	
	public Invoice() {
		
	}

	public Invoice(ArrayList<PartEntity> changedParts, AppointmentType typeOfService, Client client,
			VehicleEntity vehicle) {
		super();
		this.changedParts = changedParts;
		this.typeOfService = typeOfService;
		this.client = client;
		this.vehicle = vehicle;
	}

	public ArrayList<PartEntity> getChangedParts() {
		return changedParts;
	}

	public void setChangedParts(ArrayList<PartEntity> changedParts) {
		this.changedParts = changedParts;
	}

	public AppointmentType getTypeOfService() {
		return typeOfService;
	}

	public void setTypeOfService(AppointmentType typeOfService) {
		this.typeOfService = typeOfService;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public VehicleEntity getVehicle() {
		return vehicle;
	}

	public void setVehicle(VehicleEntity vehicle) {
		this.vehicle = vehicle;
	}

	@Override
	public String toString() {
		return "Invoice [changedParts=" + changedParts + ", typeOfService=" + typeOfService + ", client=" + client
				+ ", vehicle=" + vehicle + "]";
	}
}
