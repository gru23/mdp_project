package models.requests;

import enums.AppointmentType;
import utils.AppSession;

public class AppointmentRequest {
	private String clientId;
	private String date;
	private String time;
	private AppointmentType type;
	
	public AppointmentRequest() {
		super();
	}

	public AppointmentRequest(String date, String time, AppointmentType type) {
		super();
		this.clientId = AppSession.getInstance().getCurrentClient().getId();
		this.date = date;
		this.time = time;
		this.type = type;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public AppointmentType getType() {
		return type;
	}

	public void setType(AppointmentType type) {
		this.type = type;
	}
}
