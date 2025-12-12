package org.unibl.etf.appointment;

import java.io.Serializable;
import java.util.Objects;

import org.unibl.etf.appointment.enums.AppointmentStatus;
import org.unibl.etf.appointment.enums.AppointmentType;
import org.unibl.etf.util.XMLSerialization;

public class AppointmentEntity implements Serializable {
	private static final long serialVersionUID = 8061141856351678984L;
	
	private String id;
	/** Field date is type of String and not LocalDate because XMLDecoder can not work with newer Java classes*/
	private String date;
	/** Field time is type of String and not LocalTime because XMLDecoder can not work with newer Java classes*/
	private String time;
	private AppointmentType type;
	private AppointmentStatus status;
	private String comment;
	private String clientId;
	
	public AppointmentEntity() {
		
	}

	public AppointmentEntity(String id, String date, String time, AppointmentType type, AppointmentStatus status,
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
	
	public AppointmentEntity(String date, String time, AppointmentType type, AppointmentStatus status,
			String comment, String clientId) {
		super();
		this.id = XMLSerialization.generateId();
		this.date = date;
		this.time = time;
		this.type = type;
		this.status = status;
		this.comment = comment;
		this.clientId = clientId;
	}
	
	public AppointmentEntity(AppointmentRequest request) {
		this(request.getDate(), request.getTime(), request.getType(), AppointmentStatus.WAITING, 
				"", request.getClientId());
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppointmentEntity other = (AppointmentEntity) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "AppointmentEntity [id=" + id + ", date=" + date + ", time=" + time + ", type=" + type + ", status="
				+ status + ", comment=" + comment + ", clientId=" + clientId + "]";
	}	
}
