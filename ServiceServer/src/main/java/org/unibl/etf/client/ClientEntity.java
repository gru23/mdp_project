package org.unibl.etf.client;

import java.io.Serializable;
import java.util.Objects;

import org.unibl.etf.util.XMLSerialization;


public class ClientEntity implements Serializable {
	private static final long serialVersionUID = -5065501206513389320L;
	
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String address;
	private String phoneNumber;
	private String email;
	private AccountStatus status;
	private String vehicleId;
	
	
	public ClientEntity() {
		super();
	}

	public ClientEntity(String id, String firstName, String lastName, String username, String password, String address,
			String phoneNumber, String email, AccountStatus status, String vehicleId) {
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
		this.vehicleId = vehicleId;
	}

	public ClientEntity(String firstName, String lastName, String username, String password, String address,
			String phoneNumber, String email) {
		super();
		this.id = XMLSerialization.generateId();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = AccountStatus.UNAPPROVED;
		this.vehicleId = "";
	}
	
	public ClientEntity(ClientRequest request) {
		this(request.getFirstName(), request.getLastName(), request.getUsername(), request.getPassword(), 
				request.getAddress(), request.getPhoneNumber(), request.getEmail());
	}
	
	public ClientEntity(Client clientDTO, String password, String vehicleId) {
		this(clientDTO.getId(), clientDTO.getFirstName(), clientDTO.getLastName(), clientDTO.getUsername(), 
				password, clientDTO.getAddress(), clientDTO.getPhoneNumber(), clientDTO.getEmail(), 
				clientDTO.getStatus(), vehicleId);
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


	public String getVehicleId() {
		return vehicleId;
	}


	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientEntity other = (ClientEntity) obj;
		return Objects.equals(id, other.id);
	}


	@Override
	public String toString() {
		return "ClientEntity [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username="
				+ username + ", password=" + password + ", address=" + address + ", phoneNumber=" + phoneNumber
				+ ", email=" + email + ", status=" + status + ", vehicleId=" + vehicleId + "]";
	}	
}
