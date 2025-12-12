package servicerclientapp.models;

import servicerclientapp.enums.AppointmentStatus;
import servicerclientapp.enums.AppointmentType;

public class Appointment {
	private String id;
	private String date;
	private String time;
	private AppointmentType type;
	private AppointmentStatus status;
	private String comment;
	private String clientId;
	
	public Appointment() {
		super();
	}
	
	public Appointment(String id, String date, String time, AppointmentType type, AppointmentStatus status,
			String comment, String clientId) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
		this.type = type;
		this.status = status;
		this.comment = comment;
		this.clientId = clientId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
		return "Appointment [id=" + id + ", date=" + date + ", time=" + time + ", type=" + type + ", status=" + status
				+ ", comment=" + comment + ", clientId=" + clientId + "]";
	}
	
	
}
