package models;

public class Vehicle {
	private String id;
	private String manufacturer;
	private String model;
	private Integer year;
	private String registrationPlate;
	private String clientId;
	
	public Vehicle() {
		super();
	}

	public Vehicle(String manufacturer, String model, Integer year, String registrationPlate) {
		super();
		this.id = "";
		this.manufacturer = manufacturer;
		this.model = model;
		this.year = year;
		this.registrationPlate = registrationPlate;
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
	public String toString() {
		return "Vehicle [id=" + id + ", manufacturer=" + manufacturer + ", model=" + model + ", year=" + year
				+ ", registrationPlate=" + registrationPlate + ", clientId=" + clientId + "]";
	}	
}
