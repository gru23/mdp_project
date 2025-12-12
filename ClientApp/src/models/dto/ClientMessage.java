package models.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ClientMessage implements Serializable {
	private static final long serialVersionUID = 6245272714621412248L;
	
	private String username;
	private String message;
	private String dateAndTime;
	
	public ClientMessage() {
		
	}
	
	public ClientMessage(String username) {
		this(username, "");
	}
	
	public ClientMessage(String username, String message) {
		this.username = username;
		this.message = message;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MMM yyyy. HH:mm");
    	LocalDateTime currentTime = LocalDateTime.now();
    	this.dateAndTime = currentTime.format(formatter);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientMessage other = (ClientMessage) obj;
		return Objects.equals(username, other.username);
	}

}
