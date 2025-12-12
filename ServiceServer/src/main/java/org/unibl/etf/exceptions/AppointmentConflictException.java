package org.unibl.etf.exceptions;

public class AppointmentConflictException extends Exception {
	private static final long serialVersionUID = -7759328104252654493L;

	public AppointmentConflictException() {
		super();
	}
	
	public AppointmentConflictException(String message) {
		super(message);
	}
}
