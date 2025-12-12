package models;

import java.util.ArrayList;

import enums.AccountStatus;

public class Client {

	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String address;
	private String phoneNumber;
	private String email;
	private AccountStatus status;
	private ArrayList<String> vehiclesId;
	
	public Client() {
		super();
	}
	
	public Client(String id, String firstName, String lastName, String username, String password, String address,
			String phoneNumber, String email, AccountStatus status, ArrayList<String> vehiclesId) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = status;
		this.vehiclesId = vehiclesId;
	}
	
	public Client(String id, String firstName, String lastName, String username, String password, String address,
			String phoneNumber, String email, AccountStatus status) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public ArrayList<String> getVehiclesId() {
		return vehiclesId;
	}

	public void setVehiclesId(ArrayList<String> vehiclesId) {
		this.vehiclesId = vehiclesId;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", password=" + password + ", address=" + address + ", phoneNumber=" + phoneNumber + ", email="
				+ email + ", status=" + status + ", vehiclesId=" + vehiclesId + "]";
	}
}
