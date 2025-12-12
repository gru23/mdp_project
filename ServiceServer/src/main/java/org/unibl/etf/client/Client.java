package org.unibl.etf.client;


import java.util.Objects;

public class Client {
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String address;
	private String phoneNumber;
	private String email;
	private AccountStatus status;
	
	public Client() {
		
	}

	public Client(String id, String firstName, String lastName, String username, String address, String phoneNumber,
			String email, AccountStatus status) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.status = status;
	}
	
	public Client(ClientEntity entity) {
		this(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getUsername(), entity.getAddress(),
				entity.getPhoneNumber(), entity.getEmail(), entity.getStatus());
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username
				+ ", address=" + address + ", phoneNumber=" + phoneNumber + ", email=" + email + ", status=" + status
				+ "]";
	}	
}
