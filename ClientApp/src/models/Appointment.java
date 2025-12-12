package models;

import enums.AppointmentStatus;

import enums.AppointmentType;

public class Appointment {
	private String id;
	private String date;
	private String time;
	private String comment;
	private AppointmentType type;
	private AppointmentStatus status;
	
	
	public Appointment() {
		
	}

	public Appointment(String date, String time, String comment, AppointmentType type, AppointmentStatus status) {
		super();
		this.date = date;
		this.time = time;
		this.comment = comment;
		this.type = type;
		this.status = status;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", date=" + date + ", time=" + time + ", comment=" + comment + ", type=" + type
				+ ", status=" + status + "]";
	}	
}
