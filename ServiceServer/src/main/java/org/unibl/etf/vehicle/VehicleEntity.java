package org.unibl.etf.vehicle;

import java.io.Serializable;
import java.util.Objects;

import org.unibl.etf.util.XMLSerialization;

public class VehicleEntity implements Serializable {
	private static final long serialVersionUID = -6742495203817093924L;
	
	private String id;
	private String manufacturer;
	private String model;
	private Integer year;
	private String registrationPlate;
	private String clientId;
	
	
	public VehicleEntity() {
		super();
	}


	public VehicleEntity(String manufacturer, String model, Integer year, String registrationPlate, String clientId) {
		super();
		this.id = XMLSerialization.generateId();
		this.manufacturer = manufacturer;
		this.model = model;
		this.year = year;
		this.registrationPlate = registrationPlate;
		this.clientId = clientId;
	}
	
	public VehicleEntity(VehicleEntity vehicle) {
		this(vehicle.getManufacturer(), vehicle.getModel(), vehicle.getYear(), vehicle.getRegistrationPlate(), "");
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public Integer getYear() {
		return year;
	}


	public void setYear(Integer year) {
		this.year = year;
	}


	public String getRegistrationPlate() {
		return registrationPlate;
	}


	public void setRegistrationPlate(String registrationPlate) {
		this.registrationPlate = registrationPlate;
	}


	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VehicleEntity other = (VehicleEntity) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public String toString() {
		return "VehicleEntity [id=" + id + ", manufacturer=" + manufacturer + ", model=" + model + ", year=" + year
				+ ", registrationPlate=" + registrationPlate + ", clientId=" + clientId + "]";
	}

}
