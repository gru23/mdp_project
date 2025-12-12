package org.unibl.etf.appointment;

import org.unibl.etf.appointment.enums.AppointmentStatus;
import org.unibl.etf.appointment.enums.AppointmentType;

public class AppointmentRequest {
	private String date;
	private String time;
	private AppointmentType type;
	private AppointmentStatus status;
	private String comment;
	private String clientId;
	
	public AppointmentRequest() {
		super();
	}

	public AppointmentRequest(String date, String time, AppointmentType type, String comment, String clientId) {
		super();
		this.date = date;
		this.time = time;
		this.type = type;
		this.comment = comment;
		this.clientId = clientId;
		this.status = AppointmentStatus.WAITING;
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

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "AppointmentRequest [date=" + date + ", time=" + time + ", type=" + type + ", status=" + status
				+ ", comment=" + comment + ", clientId=" + clientId + "]";
	}	
}
